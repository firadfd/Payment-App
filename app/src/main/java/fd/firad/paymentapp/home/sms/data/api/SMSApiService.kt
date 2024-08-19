package fd.firad.paymentapp.home.sms.data.api

import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.SendSMSResponse
import fd.firad.paymentapp.home.sms.data.model.SendSmsBody
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SMSApiService {

    //3
//    @POST("subscriptions/purchase")
//    suspend fun purchase(
//        @Header("Authorization") token: String,
//        @Body request: SendSmsBody
//    ): Response<SendSMSResponse>


    //4
    @POST("sms/send")
    suspend fun sendSms(
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
        @Body request: SendSmsBody
    ): Response<SendSMSResponse>

    //5
    @GET("sms/messages")
    suspend fun allSms(
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
    ): Response<AllSMSResponse>

    //6
    @GET("sms/pending")
    suspend fun pendingSms(
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
    ): Response<AllSMSResponse>

    //8
    @POST("payment-sms/send")
    suspend fun paymentSms(
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
        @Body request: PaymentSendSmsBody
    ): Response<PaymentSMSResponse>

    //9
    @GET("get-user")
    suspend fun userInfo(
        @Header("Authorization") token: String,
    ): Response<UserInfoResponse>

    //7
    @PUT("sms/update-status/{id}")
    suspend fun updateSmsStatus(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
        @Body request: UpdateStatusBody
    ): Response<UpdateSMSStatusResponse>


}