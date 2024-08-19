package fd.firad.paymentapp.auth.data.api

import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    //1
    @POST("sign-up")
    suspend fun userSignup(
        @Body request: UserSignUpBody
    ): Response<AuthSignUpResponse>

    //2
    @POST("sign-in")
    suspend fun userSignIn(@Body request: UserSignInBody): Response<AuthSignInResponse>

//    //3
//    @POST("subscriptions/purchase")
//    suspend fun subscriptionsPurchase(
//        @Header("Authorization") token: String, @Body request: UserLogInOtpBody
//    ): Response<AuthSignInResponse>

}