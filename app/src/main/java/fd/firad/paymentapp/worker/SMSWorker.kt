package fd.firad.paymentapp.worker


//import android.content.Context
//import androidx.hilt.work.HiltWorker
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedInject
//import fd.firad.paymentapp.common.constants.Constants.sendSMS
//import fd.firad.paymentapp.common.model.ApiResponseState
//import fd.firad.paymentapp.common.utils.SharedPreferenceManager
//import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
//import fd.firad.paymentapp.home.sms.domain.usecase.SMSUseCase
//import fd.firad.paymentapp.home.sms.presentation.SendSmsResult
//
//@HiltWorker
//class SmsWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val smsUseCase: SMSUseCase,
//    private val sharedPreferenceManager: SharedPreferenceManager
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        val token = sharedPreferenceManager.getToken()
//        val apiKey = sharedPreferenceManager.getApi()
//        val secretKey = sharedPreferenceManager.getSecret()
//
//        if (token != null && apiKey != null && secretKey != null) {
//            val response = smsUseCase.pendingSms("Bearer $token", apiKey, secretKey)
//            when (response) {
//                is ApiResponseState.Success -> {
//                    val filteredList = response.data.data.distinctBy { it.id }
//                    filteredList.forEach { sms ->
//                        val result = sendSMS(applicationContext, sms.id, sms.number, sms.message)
//                        when (result) {
//                            is SendSmsResult.Success -> {
//                                smsUseCase.updateSmsStatus(
//                                    token = "Bearer $token",
//                                    id = sms.id,
//                                    apiKey = apiKey,
//                                    secretKey = secretKey,
//                                    request = UpdateStatusBody(status = "sent")
//                                )
//                            }
//
//                            is SendSmsResult.Failure -> {
//                                // Handle failure
//                            }
//                        }
//                    }
//                }
//
//                is ApiResponseState.Error -> {
//                    // Handle error
//                }
//
//                is ApiResponseState.Loading -> { /* Show loading if needed */
//                }
//            }
//        }
//
//        return Result.success()
//    }
//}
