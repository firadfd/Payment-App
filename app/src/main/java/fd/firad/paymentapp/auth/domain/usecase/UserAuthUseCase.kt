package fd.firad.paymentapp.auth.domain.usecase

import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.ForgotPasswordBody
import fd.firad.paymentapp.auth.data.model.ForgotPasswordResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.data.model.VerifyOTPBody
import fd.firad.paymentapp.auth.domain.repository.AuthRepository
import fd.firad.paymentapp.common.model.ApiResponseState
import javax.inject.Inject

class UserAuthUseCase @Inject constructor(private val userRepository: AuthRepository) {

    suspend fun userSignup(
        request: UserSignUpBody
    ): ApiResponseState<AuthSignUpResponse> {
        return userRepository.userSignup(request)
    }

    suspend fun userSignIn(
        request: UserSignInBody
    ): ApiResponseState<AuthSignInResponse> {
        return userRepository.userSignIn(request)
    }

    suspend fun userForgotPassword(request: ForgotPasswordBody
    ): ApiResponseState<ForgotPasswordResponse>{
        return userRepository.userForgotPassword(request)
    }
    suspend fun userVerifyOtp(request: VerifyOTPBody
    ): ApiResponseState<ForgotPasswordResponse>{
        return userRepository.userVerifyOtp(request)
    }

}