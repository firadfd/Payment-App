package fd.firad.paymentapp.auth.data.model

data class AuthSignUpResponse(
    val status: Boolean, val message: String, val user: UserPayload
)

data class UserPayload(
    val name: String,
    val email: String,
    val phone: String,
    val updated_at: String,
    val created_at: String,
    val id: Int,
)

data class AuthSignInResponse(
    val status: Boolean, val token: String, val message: String, val token_type: String
)

data class ForgotPasswordResponse(
    val status: Boolean, val message: String, val errors: String? = null
)

