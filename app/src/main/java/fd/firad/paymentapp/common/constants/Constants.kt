package fd.firad.paymentapp.common.constants

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import fd.firad.paymentapp.home.sms.presentation.SendSmsResult
import java.io.ByteArrayOutputStream
import java.io.IOException

object Constants {
    const val BASE_URL = "https://api.codexen.co/api/"
    const val API_KEY = ""

    fun isOnline(@ApplicationContext context: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                if (activeNetwork.isConnected) {
                    haveConnectedWifi = true
                }
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                if (activeNetwork.isConnected) {
                    haveConnectedMobile = true
                }
            }
        }

        return haveConnectedWifi || haveConnectedMobile
    }


    fun sendSMS(context: Context, smsId: Int, phoneNumber: String, message: String): SendSmsResult {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return SendSmsResult.Failure(Throwable("Permission to read phone state is not granted."))
        }

        try {
            val subscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val subscriptionList = subscriptionManager.activeSubscriptionInfoList

            if (!subscriptionList.isNullOrEmpty()) {
                val subscriptionInfo = subscriptionList[0]
                val smsManager =
                    SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo.subscriptionId)
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                return SendSmsResult.Success("SMS sent with ID: $smsId")
            } else {
                return SendSmsResult.Failure(Throwable("No SIM cards available"))
            }
        } catch (e: Exception) {
            return SendSmsResult.Failure(Throwable("Failed to send SMS: ${e.message}"))
        }
    }

}

fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$""")
    return emailRegex.matches(email)
}

private const val MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024 // 2 MB

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    var inputStream = context.contentResolver.openInputStream(uri) ?: return null
    return try {
        // First, check the size of the image
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        val imageSizeBytes =
            options.outWidth * options.outHeight * 4 // Estimation of image size in bytes

        // Reset the input stream to read the full image
        inputStream.close()
        inputStream = context.contentResolver.openInputStream(uri) ?: return null

        // Resize the image if it exceeds the size limit
        var bitmap: Bitmap? = null
        if (imageSizeBytes > MAX_IMAGE_SIZE_BYTES) {
            // Calculate scale factor to resize the image
            val scale = Math.sqrt(imageSizeBytes.toDouble() / MAX_IMAGE_SIZE_BYTES.toDouble())
            val scaledWidth = (options.outWidth / scale).toInt()
            val scaledHeight = (options.outHeight / scale).toInt()

            // Decode the image with the calculated dimensions
            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            bitmap = bitmap?.let { Bitmap.createScaledBitmap(it, scaledWidth, scaledHeight, true) }
        } else {
            // Decode the image without resizing
            bitmap = BitmapFactory.decodeStream(inputStream)
        }

        // Convert Bitmap to ByteArray
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        // Clean up resources
        bitmap?.recycle()
        outputStream.close()

        byteArray
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
