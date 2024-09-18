package fd.firad.paymentapp.home.sms.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.local.db.UserInfoDao
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.remote.SMSApiService
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.entity.SmsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SMSRepositoryImpl @Inject constructor(
    private val smsApiService: SMSApiService,
    private val smsDao: SmsDao,
    private val userInfoDao: UserInfoDao,
    @ApplicationContext private val context: Context,
    private val connectivityManager: ConnectivityManager
) : SMSRepository {
    override suspend fun insertSms(smsEntity: SmsEntity) {
        smsDao.insertSms(smsEntity)
    }

    override fun getAllSms(): LiveData<List<SmsEntity>> {
        return smsDao.getAllSms()
    }

    override suspend fun deleteSms(smsEntity: SmsEntity) {
        smsDao.deleteSms(smsEntity)
    }

    override suspend fun allSms(
        token: String, apiKey: String, secretKey: String
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
        token: String, apiKey: String, secretKey: String
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
        token: String, apiKey: String, secretKey: String, request: PaymentSendSmsBody
    ): ApiResponseState<PaymentSMSResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.paymentSms(
                    token = token, apiKey = apiKey, secretKey = secretKey, request = request
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

    override suspend fun userInfo(
        token: String, forceFetch: Boolean
    ): ApiResponseState<UserInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        Log.e("UserInfoRepository", "No internet connection. Cannot fetch data.")
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Log the force fetch scenario
                    Log.e(
                        "UserInfoRepository",
                        "Force fetch is true. Clearing local data and fetching from API."
                    )

                    // Clear the local data in Room
                    userInfoDao.clearUserInfo()

                    // Fetch from the API
                    val apiResponse = smsApiService.userInfo(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            userInfoDao.insertUserInfo(it)

                            // Fetch the data again from Room after saving
                            val savedData = userInfoDao.getUserInfo()
                            if (savedData != null) {
                                // Log the successful fetch and save
                                ApiResponseState.Success(savedData)
                            } else {
                                // Log failure to fetch data from Room
                                ApiResponseState.Error("Failed to fetch saved data from Room")
                            }
                        } ?: run {
                            // Log null response body
                            ApiResponseState.Error("Response body is null")
                        }
                    } else {
                        // Log API invalid response
                        ApiResponseState.Error("API returned invalid response")
                    }
                } else {
                    // Log non-force fetch scenario
                    Log.e(
                        "UserInfoRepository", "Force fetch is false. Fetching from local Room data."
                    )

                    // Try to get data from Room
                    val localData = userInfoDao.getUserInfo()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.userInfo(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                userInfoDao.insertUserInfo(it)

                                // Fetch the data again from Room after saving
                                val savedData = userInfoDao.getUserInfo()
                                if (savedData != null) {
                                    // Log the successful fetch and save
                                    Log.e(
                                        "UserInfoRepository",
                                        "Data fetched from API and saved to Room."
                                    )
                                    ApiResponseState.Success(savedData)
                                } else {
                                    // Log failure to fetch data from Room
                                    ApiResponseState.Error("Failed to fetch saved data from Room")
                                }
                            } ?: run {
                                // Log null response body
                                ApiResponseState.Error("Response body is null")
                            }
                        } else {
                            // Log API invalid response
                            ApiResponseState.Error("API returned invalid response")
                        }
                    }
                }
            } catch (e: IOException) {
                // Log network-related exceptions
                ApiResponseState.Error("Network error: ${e.message}")
            } catch (e: SocketTimeoutException) {
                // Log timeout exceptions
                ApiResponseState.Error("Request timed out: ${e.message}")
            } catch (e: HttpException) {
                // Log HTTP protocol-related exceptions
                ApiResponseState.Error("HTTP error: ${e.message()}")
            } catch (e: CancellationException) {
                // Log request cancellations
                ApiResponseState.Error("Request was cancelled")
            } catch (e: Exception) {
                // Log any other unanticipated exceptions
                ApiResponseState.Error("An unknown error occurred: ${e.message}")
            }
        }
    }


    override suspend fun updateStatus(
        token: String, id: Int, apiKey: String, secretKey: String, request: UpdateStatusBody
    ): ApiResponseState<UpdateSMSStatusResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.updateSmsStatus(
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

    override suspend fun todayTransaction(token: String): ApiResponseState<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.todayTransaction(
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

    override suspend fun weekTransaction(token: String): ApiResponseState<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.weeklyTransaction(
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

    override suspend fun monthTransaction(token: String): ApiResponseState<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.monthlyTransaction(
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

    override suspend fun yearTransaction(token: String): ApiResponseState<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.yearlyTransaction(
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

    override suspend fun allTransaction(token: String): ApiResponseState<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = smsApiService.alltimeTransaction(
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


    private fun checkInternetConnectivity(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            null
        }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}

