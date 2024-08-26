package fd.firad.paymentapp.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import fd.firad.paymentapp.MainActivity
import fd.firad.paymentapp.R
import fd.firad.paymentapp.common.constants.Constants.isOnline
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.domain.usecase.SMSUseCase
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.entity.SmsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class SendSmsService : IntentService("SendSmsService") {
    @Inject
    lateinit var smsUseCase: SMSUseCase

    @Inject
    lateinit var smsDao: SmsDao

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferenceManager

    override fun onHandleIntent(intent: Intent?) {
        val sender = intent?.getStringExtra("sender")
        val message = intent?.getStringExtra("message")


        if (isOnline(applicationContext)) {
            if (sharedPreferencesManager.getToken() != null && sharedPreferencesManager.getApi() != null && sharedPreferencesManager.getSecret() != null) {
                if (sender!!.toLowerCase() in listOf("bkash", "nagad", "16216", "upay")) {
                    val job = GlobalScope.launch(Dispatchers.Default) {
                        try {
                            val result = smsUseCase.paymentSms(
                                token = "Bearer ${sharedPreferencesManager.getToken()!!}",
                                apiKey = sharedPreferencesManager.getApi()!!,
                                secretKey = sharedPreferencesManager.getSecret()!!,
                                request = PaymentSendSmsBody(
                                    sender = sender.toLowerCase(), sms_text = message!!
                                )
                            )
                            when (result) {
                                is ApiResponseState.Success -> {
                                    if (result.data.status) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                applicationContext,
                                                result.data.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                applicationContext,
                                                result.data.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }

                                is ApiResponseState.Error -> {
                                    val errorMessage = result.errorMessage
                                    Toast.makeText(
                                        applicationContext, errorMessage, Toast.LENGTH_SHORT
                                    ).show()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            applicationContext, errorMessage, Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                is ApiResponseState.Loading -> {
                                }

                            }
                        } catch (e: Exception) {
                            smsDao.insertSms(
                                SmsEntity(
                                    sender = sender!!, msg = message!!
                                )
                            )
                        }
                    }
                    runBlocking {
                        job.join()
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please Purchase Subscription To Active Payment SMS Features",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            val job2 = GlobalScope.launch(Dispatchers.Default) {
                when (sender!!.toLowerCase()) {
                    "bkash" -> {
                        try {
                            smsDao.insertSms(
                                SmsEntity(
                                    sender = sender, msg = message!!
                                )
                            )
                            showNotification()

                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext, e.toString(), Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    "nagad" -> {
                        try {
                            smsDao.insertSms(
                                SmsEntity(
                                    sender = sender, msg = message!!
                                )
                            )
                            showNotification()

                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext, e.toString(), Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    "16216" -> {
                        try {
                            smsDao.insertSms(
                                SmsEntity(
                                    sender = sender, msg = message!!
                                )
                            )
                            showNotification()

                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext, e.toString(), Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    "upay" -> {
                        try {
                            smsDao.insertSms(
                                SmsEntity(
                                    sender = sender, msg = message!!
                                )
                            )
                            showNotification()

                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext, e.toString(), Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    else -> {

                    }
                }


            }
            runBlocking {
                job2.join()
            }

        }

    }

    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "PaymentChannelId", "Channel Name", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "masterChannelId")
            .setContentTitle("SMS Sync").setContentText("You are offline SMS not sent")
            .setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX).setAutoCancel(false).build()
        val notificationId = 90
        notificationManager.notify(notificationId, notificationBuilder)
    }

}