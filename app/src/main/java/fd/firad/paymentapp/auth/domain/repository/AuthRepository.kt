package fd.firad.paymentapp.auth.domain.repository

import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.ForgotPasswordBody
import fd.firad.paymentapp.auth.data.model.ForgotPasswordResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.data.model.VerifyOTPBody
import fd.firad.paymentapp.common.model.ApiResponseState

interface AuthRepository {
    suspend fun userSignup(request: UserSignUpBody): ApiResponseState<AuthSignUpResponse>
    suspend fun userSignIn(request: UserSignInBody): ApiResponseState<AuthSignInResponse>
    suspend fun userForgotPassword(request: ForgotPasswordBody): ApiResponseState<ForgotPasswordResponse>
    suspend fun userVerifyOtp(request: VerifyOTPBody): ApiResponseState<ForgotPasswordResponse>

}