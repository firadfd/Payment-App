package fd.firad.paymentapp.home.sms.domain.repository

import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse

interface SMSRepository {

    suspend fun allSms(
        token: String,
        apiKey: String,
        secretKey: String,
    ): ApiResponseState<AllSMSResponse>

    suspend fun pendingSms(
        token: String,
        apiKey: String,
        secretKey: String,
    ): ApiResponseState<AllSMSResponse>


    suspend fun paymentSms(
        token: String,
        apiKey: String,
        secretKey: String,
        request: PaymentSendSmsBody
    ): ApiResponseState<PaymentSMSResponse>

    suspend fun userInfo(
        token: String
    ): ApiResponseState<UserInfoResponse>

    suspend fun updateStatus(
        token: String,
        id: Int,
        apiKey: String,
        secretKey: String,
        request: UpdateStatusBody
    ): ApiResponseState<UpdateSMSStatusResponse>

}