package fd.firad.paymentapp.home.sms.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.AllTimeTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.ChangePasswordBody
import fd.firad.paymentapp.home.sms.data.model.MonthlyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdatePassResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.model.WeeklyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.YearlyTransactionResponse
import fd.firad.paymentapp.home.sms.data.remote.SMSApiService
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import fd.firad.paymentapp.room.database.SmsDatabase
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
    private val database: SmsDatabase,
    @ApplicationContext private val context: Context,
    private val connectivityManager: ConnectivityManager
) : SMSRepository {
    override suspend fun insertSms(smsEntity: SmsEntity) {
        database.smsDao().insertSms(smsEntity)
    }

    override fun getAllSms(): LiveData<List<SmsEntity>> {
        return database.smsDao().getAllSms()
    }

    override suspend fun deleteSms(smsEntity: SmsEntity) {
        database.smsDao().deleteSms(smsEntity)
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
                    database.userInfoDao().clearUserInfo()

                    // Fetch from the API
                    val apiResponse = smsApiService.userInfo(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.userInfoDao().insertUserInfo(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.userInfoDao().getUserInfo()
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
                    val localData = database.userInfoDao().getUserInfo()
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
                                database.userInfoDao().insertUserInfo(it)

                                // Fetch the data again from Room after saving
                                val savedData = database.userInfoDao().getUserInfo()
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

    override suspend fun todayTransaction(
        token: String, forceFetch: Boolean
    ): ApiResponseState<TodayTransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Clear the local data in Room
                    database.todayTransactionDao().clearTodayTransaction()

                    // Fetch from the API
                    val apiResponse = smsApiService.todayTransaction(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.todayTransactionDao().insertTodayTransaction(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.todayTransactionDao().getTodayTransaction()
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
                    val localData = database.todayTransactionDao().getTodayTransaction()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.todayTransaction(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                database.todayTransactionDao().insertTodayTransaction(it)

                                // Fetch the data again from Room after saving
                                val savedData = database.todayTransactionDao().getTodayTransaction()
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

    override suspend fun weekTransaction(
        token: String, forceFetch: Boolean
    ): ApiResponseState<WeeklyTransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Clear the local data in Room
                    database.weeklyTransactionDao().clearWeeklyTransaction()

                    // Fetch from the API
                    val apiResponse = smsApiService.weeklyTransaction(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.weeklyTransactionDao().insertWeeklyTransaction(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.weeklyTransactionDao().getWeeklyTransaction()
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
                    val localData = database.weeklyTransactionDao().getWeeklyTransaction()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.weeklyTransaction(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                database.weeklyTransactionDao().insertWeeklyTransaction(it)

                                // Fetch the data again from Room after saving
                                val savedData =
                                    database.weeklyTransactionDao().getWeeklyTransaction()
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

    override suspend fun monthTransaction(
        token: String, forceFetch: Boolean
    ): ApiResponseState<MonthlyTransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Clear the local data in Room
                    database.monthlyTransactionDao().clearMonthlyTransaction()

                    // Fetch from the API
                    val apiResponse = smsApiService.monthlyTransaction(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.monthlyTransactionDao().insertMonthlyTransaction(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.monthlyTransactionDao().getMonthlyTransaction()
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
                    val localData = database.monthlyTransactionDao().getMonthlyTransaction()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.monthlyTransaction(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                database.monthlyTransactionDao().insertMonthlyTransaction(it)

                                // Fetch the data again from Room after saving
                                val savedData = database.monthlyTransactionDao().getMonthlyTransaction()
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

    override suspend fun yearTransaction(
        token: String, forceFetch: Boolean
    ): ApiResponseState<YearlyTransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Clear the local data in Room
                    database.yearlyTransactionDao().clearYearlyTransaction()

                    // Fetch from the API
                    val apiResponse = smsApiService.yearlyTransaction(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.yearlyTransactionDao().insertYearlyTransaction(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.yearlyTransactionDao().getYearlyTransaction()
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
                    val localData = database.yearlyTransactionDao().getYearlyTransaction()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.yearlyTransaction(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                database.yearlyTransactionDao().insertYearlyTransaction(it)

                                // Fetch the data again from Room after saving
                                val savedData = database.yearlyTransactionDao().getYearlyTransaction()
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

    override suspend fun allTransaction(
        token: String, forceFetch: Boolean
    ): ApiResponseState<AllTimeTransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceFetch) {

                    val isConnected = checkInternetConnectivity()
                    if (!isConnected) {
                        // Notify user to connect to the internet
                        return@withContext ApiResponseState.Error("No internet connection. Please check your connection and try again.")
                    }

                    // Clear the local data in Room
                    database.allTimeTransactionDao().clearAllTimeTransaction()

                    // Fetch from the API
                    val apiResponse = smsApiService.alltimeTransaction(token = token)

                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let {
                            // Save the new data to Room
                            database.allTimeTransactionDao().insertAllTimeTransaction(it)

                            // Fetch the data again from Room after saving
                            val savedData = database.allTimeTransactionDao().getAllTimeTransaction()
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
                    val localData = database.allTimeTransactionDao().getAllTimeTransaction()
                    if (localData != null) {
                        // Log the successful fetch from Room
                        Log.e("UserInfoRepository", "Data fetched from Room.")
                        ApiResponseState.Success(localData)
                    } else {
                        // Log that local data is not available and proceed with API call
                        Log.e("UserInfoRepository", "No data in Room. Fetching from API.")

                        // Fetch from the API
                        val apiResponse = smsApiService.alltimeTransaction(token = token)

                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let {
                                // Save the new data to Room
                                database.allTimeTransactionDao().insertAllTimeTransaction(it)

                                // Fetch the data again from Room after saving
                                val savedData = database.allTimeTransactionDao().getAllTimeTransaction()
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

    override suspend fun changePassword(
        token: String,
        request: ChangePasswordBody
    ): ApiResponseState<UpdatePassResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    smsApiService.changePassword(token = token, request = request)
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
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}

