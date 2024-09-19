package fd.firad.paymentapp.home.sms.domain.repository

import androidx.lifecycle.LiveData
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.AllTimeTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.MonthlyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.model.WeeklyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.YearlyTransactionResponse
import fd.firad.paymentapp.room.entity.SmsEntity

interface SMSRepository {

    suspend fun insertSms(smsEntity: SmsEntity)
    fun getAllSms(): LiveData<List<SmsEntity>>
    suspend fun deleteSms(smsEntity: SmsEntity)

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
        token: String, forceFetch: Boolean
    ): ApiResponseState<UserInfoResponse>


    suspend fun updateStatus(
        token: String,
        id: Int,
        apiKey: String,
        secretKey: String,
        request: UpdateStatusBody
    ): ApiResponseState<UpdateSMSStatusResponse>

    suspend fun todayTransaction(token: String,forceFetch: Boolean): ApiResponseState<TodayTransactionResponse>
    suspend fun weekTransaction(token: String,forceFetch: Boolean): ApiResponseState<WeeklyTransactionResponse>
    suspend fun monthTransaction(token: String,forceFetch: Boolean): ApiResponseState<MonthlyTransactionResponse>
    suspend fun yearTransaction(token: String,forceFetch: Boolean): ApiResponseState<YearlyTransactionResponse>
    suspend fun allTransaction(token: String,forceFetch: Boolean): ApiResponseState<AllTimeTransactionResponse>

}