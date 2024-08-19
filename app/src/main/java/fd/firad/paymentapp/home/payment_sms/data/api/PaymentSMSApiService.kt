package fd.firad.paymentapp.home.payment_sms.data.api

import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentSMSApiService {
    //8
    @POST("payment-sms/send")
    suspend fun paymentSms(
        @Header("Authorization") token: String,
        @Header("api-key") apiKey: String,
        @Header("secret-key") secretKey: String,
        @Body request: PaymentSendSmsBody
    ): Response<PaymentSMSResponse>
}