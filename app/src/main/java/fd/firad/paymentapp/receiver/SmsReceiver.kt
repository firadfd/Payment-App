package fd.firad.paymentapp.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import fd.firad.paymentapp.service.SendSmsService

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras

            var senderNumbers = ""
            var messageBodies = ""

            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                val messages = arrayOfNulls<SmsMessage>(pdus.size)
                for (i in messages.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    if (i == 0) {
                        senderNumbers = messages[i]?.originatingAddress ?: ""
                    }
                    messageBodies += messages[i]?.messageBody ?: ""
                }

                sendDataToApi(
                    context,
                    sender = senderNumbers,
                    messageBodies = messageBodies
                )

            }
        }
    }

    //send all data to SendSmsService
    private fun sendDataToApi(
        context: Context?, sender: String, messageBodies: String
    ) {
        val intent = Intent(context, SendSmsService::class.java)
        intent.putExtra("sender", sender)
        intent.putExtra("message", messageBodies)
        context?.startService(intent)
    }
}