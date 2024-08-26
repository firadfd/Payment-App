package fd.firad.paymentapp.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import fd.firad.paymentapp.R
import fd.firad.paymentapp.common.constants.Constants.sendSMS
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.domain.usecase.SMSUseCase
import fd.firad.paymentapp.home.sms.presentation.SendSmsResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsService : Service() {

    @Inject
    lateinit var smsUseCase: SMSUseCase

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val notification: Notification = createNotification()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val token = sharedPreferenceManager.getToken()
            val apiKey = sharedPreferenceManager.getApi()
            val secretKey = sharedPreferenceManager.getSecret()

            if (token != null && apiKey != null && secretKey != null) {
                val response = smsUseCase.pendingSms("Bearer $token", apiKey, secretKey)
                when (response) {
                    is ApiResponseState.Success -> {
                        val filteredList =
                            response.data.data.distinctBy { it.id }.filter { it.status != "sent" }
                        filteredList.forEach { sms ->
                            val result =
                                sendSMS(applicationContext, sms.id, sms.number, sms.message)
                            when (result) {
                                is SendSmsResult.Success -> {
                                    smsUseCase.updateSmsStatus(
                                        token = "Bearer $token",
                                        id = sms.id,
                                        apiKey = apiKey,
                                        secretKey = secretKey,
                                        request = UpdateStatusBody(status = "sent")
                                    )
                                }

                                is SendSmsResult.Failure -> {
                                    Toast.makeText(
                                        applicationContext,
                                        result.error.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            delay(3000)
                        }
                    }

                    is ApiResponseState.Error -> {

                    }

                    is ApiResponseState.Loading -> {
                    }
                }
//                delay(3000)
            }
//            delay(120000)
        }

        setUpAlarm()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun createNotification(): Notification {
        val channelId = "ForegroundServiceChannel"
        val channel = NotificationChannel(
            channelId, "Foreground Service", NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return Notification.Builder(this, channelId)
            .setContentTitle("SMS Service Running")
            .setContentText("Sending pending SMS messages...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private fun setUpAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SmsService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intervalMillis = 60000L // 1 minute
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + intervalMillis,
            intervalMillis,
            pendingIntent
        )
    }
}
