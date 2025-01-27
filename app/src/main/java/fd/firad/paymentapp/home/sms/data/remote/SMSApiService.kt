package fd.firad.paymentapp.home.sms.data.remote

import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.AllTimeTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.ChangePasswordBody
import fd.firad.paymentapp.home.sms.data.model.MonthlyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.SendSMSResponse
import fd.firad.paymentapp.home.sms.data.model.SendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdatePassResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UpdateUserResponse
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.model.WeeklyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.YearlyTransactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    //10
    @PUT("sms/update-status/{id}")
    suspend fun updateSmsStatus(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
        @Body request: UpdateStatusBody
    ): Response<UpdateSMSStatusResponse>

    //11
    @GET("todaytransaction")
    suspend fun todayTransaction(
        @Header("Authorization") token: String,
    ): Response<TodayTransactionResponse>


    //12
    @GET("weeklytransaction")
    suspend fun weeklyTransaction(
        @Header("Authorization") token: String,
    ): Response<WeeklyTransactionResponse>


    //13
    @GET("monthlytransaction")
    suspend fun monthlyTransaction(
        @Header("Authorization") token: String,
    ): Response<MonthlyTransactionResponse>

    //14
    @GET("yearlytransaction")
    suspend fun yearlyTransaction(
        @Header("Authorization") token: String,
    ): Response<YearlyTransactionResponse>

    //14
    @GET("alltimetransaction")
    suspend fun alltimeTransaction(
        @Header("Authorization") token: String,
    ): Response<AllTimeTransactionResponse>

    //15
    @Multipart
    @POST("update-user")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part profileImage: MultipartBody.Part
    ): Response<UpdateUserResponse>


    //16
    @POST("change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordBody
    ):Response<UpdatePassResponse>


}