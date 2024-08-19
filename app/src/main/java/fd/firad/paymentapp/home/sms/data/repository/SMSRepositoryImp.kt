package fd.firad.paymentapp.home.sms.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.api.SMSApiService
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SMSRepositoryImpl @Inject constructor(private val smsApiService: SMSApiService) :
    SMSRepository {
    override suspend fun allSms(
        token: String,
        apiKey: String,
        secretKey: String
    ): ApiResponseState<AllSMSResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.allSms(token = token, apiKey = apiKey, secretKey = secretKey)
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {

                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("message")?.asString ?: "Unknown error"
                    }
                    ApiResponseState.Error("Error: ${response.code()} - $errorMessage")
                }
            } catch (e: IOException) {
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

    override suspend fun pendingSms(
        token: String,
        apiKey: String,
        secretKey: String
    ): ApiResponseState<AllSMSResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.pendingSms(token = token, apiKey = apiKey, secretKey = secretKey)
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {

                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("message")?.asString ?: "Unknown error"
                    }
                    ApiResponseState.Error("Error: ${response.code()} - $errorMessage")
                }
            } catch (e: IOException) {
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }



    override suspend fun paymentSms(
        token: String,
        apiKey: String,
        secretKey: String,
        request: PaymentSendSmsBody
    ): ApiResponseState<PaymentSMSResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.paymentSms(
                        token = token,
                        apiKey = apiKey,
                        secretKey = secretKey,
                        request = request
                    )
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {

                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("message")?.asString ?: "Unknown error"
                    }
                    ApiResponseState.Error("Error: ${response.code()} - $errorMessage")
                }
            } catch (e: IOException) {
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

    override suspend fun userInfo(token: String): ApiResponseState<UserInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.userInfo(
                        token = token
                    )
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {

                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("message")?.asString ?: "Unknown error"
                    }
                    ApiResponseState.Error("Error: ${response.code()} - $errorMessage")
                }
            } catch (e: IOException) {
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

    override suspend fun updateStatus(
        token: String,
        id: Int,
        apiKey: String,
        secretKey: String,
        request: UpdateStatusBody
    ): ApiResponseState<UpdateSMSStatusResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.updateSmsStatus(
                        id = id,
                        token = token,
                        apiKey = apiKey,
                        secretKey = secretKey,
                        request = request
                    )
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {

                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("message")?.asString ?: "Unknown error"
                    }
                    ApiResponseState.Error("Error: ${response.code()} - $errorMessage")
                }
            } catch (e: IOException) {
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

}