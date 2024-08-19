package fd.firad.paymentapp.home.sms.domain.usecase

import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import javax.inject.Inject

class SMSUseCase @Inject constructor(private val smsRepository: SMSRepository) {

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
        token: String
    ): ApiResponseState<UserInfoResponse> {
        return smsRepository.userInfo(token)
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
}