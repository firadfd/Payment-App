package fd.firad.paymentapp.home.sms.data.model

import java.io.File

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

data class UpdateUserResponse(
    val status: Boolean,
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val device_id: String? = null,
    val status: String,
    val role:String,
    val profile_image: String? = null,
    val email_verified_at: String? = null,
    val created_at: String,
    val updated_at: String
)

data class ChangePasswordBody(
    val old_password: String,
    val new_password: String
)

data class UpdatePassResponse(
    val status: Boolean,
    val message: String
)