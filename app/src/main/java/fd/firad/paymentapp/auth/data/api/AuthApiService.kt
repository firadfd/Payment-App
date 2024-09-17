package fd.firad.paymentapp.auth.data.api

import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.ForgotPasswordBody
import fd.firad.paymentapp.auth.data.model.ForgotPasswordResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.data.model.VerifyOTPBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("sign-up")
    suspend fun userSignup(
        @Body request: UserSignUpBody
    ): Response<AuthSignUpResponse>


    @POST("sign-in")
    suspend fun userSignIn(@Body request: UserSignInBody): Response<AuthSignInResponse>


    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordBody): Response<ForgotPasswordResponse>


    @POST("verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOTPBody): Response<ForgotPasswordResponse>


//    @POST("subscriptions/purchase")
//    suspend fun subscriptionsPurchase(
//        @Header("Authorization") token: String, @Body request: UserLogInOtpBody
//    ): Response<AuthSignInResponse>

}