package fd.firad.paymentapp.auth.data.model

data class UserSignUpBody(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
)

data class UserSignInBody(val login: String, val password: String)

data class ForgotPasswordBody(val login: String)

data class VerifyOTPBody(
    val login: String,
    val otp: String,
    val password: String,
)
