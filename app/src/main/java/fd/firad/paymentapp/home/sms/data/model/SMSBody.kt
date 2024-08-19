package fd.firad.paymentapp.home.sms.data.model

data class SendSmsBody(
    val number: String,
    val message: String
)

data class PaymentSendSmsBody(
    val sender: String,
    val sms_text: String
)

data class UpdateStatusBody(
    val status: String = "sent"
)