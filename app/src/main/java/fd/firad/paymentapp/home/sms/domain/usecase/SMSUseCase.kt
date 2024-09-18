package fd.firad.paymentapp.home.sms.domain.usecase

import androidx.lifecycle.LiveData
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
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
        token: String,forceFetch: Boolean
    ): ApiResponseState<UserInfoResponse> {
        return smsRepository.userInfo(token,forceFetch)
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
        token: String,
    ): ApiResponseState<TransactionResponse> {
        return smsRepository.todayTransaction(
            token = token
        )
    }

    suspend fun weeklyTransaction(
        token: String,
    ): ApiResponseState<TransactionResponse> {
        return smsRepository.weekTransaction(
            token = token
        )
    }

    suspend fun monthlyTransaction(
        token: String,
    ): ApiResponseState<TransactionResponse> {
        return smsRepository.monthTransaction(
            token = token
        )
    }

    suspend fun yearlyTransaction(
        token: String,
    ): ApiResponseState<TransactionResponse> {
        return smsRepository.yearTransaction(
            token = token
        )
    }

    suspend fun allTimeTransaction(
        token: String,
    ): ApiResponseState<TransactionResponse> {
        return smsRepository.allTransaction(
            token = token
        )
    }
}
