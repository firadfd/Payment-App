package fd.firad.paymentapp.home.sms.domain.usecase

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
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import fd.firad.paymentapp.room.entity.SmsEntity
import javax.inject.Inject

class SMSUseCase @Inject constructor(private val smsRepository: SMSRepository) {

    fun getFailedSms(): LiveData<List<SmsEntity>> {
        return smsRepository.getAllSms()
    }

    suspend fun insertSms(smsEntity: SmsEntity) {
        smsRepository.insertSms(smsEntity)
    }

    suspend fun deleteSms(smsEntity: SmsEntity) {
        smsRepository.deleteSms(smsEntity)
    }

    suspend fun allSms(
        token: String,
        apiKey: String,
        secretKey: String
    ): ApiResponseState<AllSMSResponse> {
        return smsRepository.allSms(token, apiKey, secretKey)
    }

    suspend fun pendingSms(
        token: String,
        apiKey: String,
        secretKey: String,
    ): ApiResponseState<AllSMSResponse> {
        return smsRepository.pendingSms(token, apiKey, secretKey)
    }

    suspend fun paymentSms(
        token: String,
        apiKey: String,
        secretKey: String,
        request: PaymentSendSmsBody
    ): ApiResponseState<PaymentSMSResponse> {
        return smsRepository.paymentSms(token, apiKey, secretKey, request)
    }

    suspend fun userInfo(
        token: String, forceFetch: Boolean
    ): ApiResponseState<UserInfoResponse> {
        return smsRepository.userInfo(token, forceFetch)
    }

    suspend fun updateSmsStatus(
        token: String,
        id: Int,
        apiKey: String,
        secretKey: String,
        request: UpdateStatusBody
    ): ApiResponseState<UpdateSMSStatusResponse> {
        return smsRepository.updateStatus(
            token = token,
            id = id,
            apiKey = apiKey,
            secretKey = secretKey,
            request = request
        )
    }


    suspend fun todayTransaction(
        token: String,forceFetch: Boolean
    ): ApiResponseState<TodayTransactionResponse> {
        return smsRepository.todayTransaction(
            token = token,forceFetch
        )
    }

    suspend fun weeklyTransaction(
        token: String,forceFetch: Boolean
    ): ApiResponseState<WeeklyTransactionResponse> {
        return smsRepository.weekTransaction(
            token = token,forceFetch
        )
    }

    suspend fun monthlyTransaction(
        token: String,forceFetch: Boolean
    ): ApiResponseState<MonthlyTransactionResponse> {
        return smsRepository.monthTransaction(
            token = token,forceFetch
        )
    }

    suspend fun yearlyTransaction(
        token: String,forceFetch: Boolean
    ): ApiResponseState<YearlyTransactionResponse> {
        return smsRepository.yearTransaction(
            token = token,forceFetch
        )
    }

    suspend fun allTimeTransaction(
        token: String,forceFetch: Boolean
    ): ApiResponseState<AllTimeTransactionResponse> {
        return smsRepository.allTransaction(
            token = token,forceFetch
        )
    }
}
