package fd.firad.paymentapp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import fd.firad.paymentapp.nav.RootNavHost
import fd.firad.paymentapp.service.SmsService
import fd.firad.paymentapp.ui.theme.PaymentAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setUpAlarm()
        enableEdgeToEdge()
        setContent {
            PaymentAppTheme {
                RootNavHost(navController = rememberNavController())
            }

        }
    }

    private fun setUpAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SmsService::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val intervalMillis = 60000L // 1 minute
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + intervalMillis,
            intervalMillis,
            pendingIntent
        )
    }


}
