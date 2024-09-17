package fd.firad.paymentapp.nav

import kotlinx.serialization.Serializable

@Serializable
object ScreenLogin

@Serializable
object ScreenRegistration

@Serializable
object ScreenForgetPassword

@Serializable
data class ScreenForgetPasswordOTP(val email: String)