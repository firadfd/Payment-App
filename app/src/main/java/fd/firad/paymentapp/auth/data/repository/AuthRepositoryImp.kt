package fd.firad.paymentapp.auth.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import fd.firad.paymentapp.auth.data.api.AuthApiService
import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.ForgotPasswordBody
import fd.firad.paymentapp.auth.data.model.ForgotPasswordResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.data.model.VerifyOTPBody
import fd.firad.paymentapp.auth.domain.repository.AuthRepository
import fd.firad.paymentapp.common.model.ApiResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


class AuthRepositoryImpl @Inject constructor(private val authApiService: AuthApiService) :
    AuthRepository {
    override suspend fun userSignup(
        request: UserSignUpBody
    ): ApiResponseState<AuthSignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.userSignup(request)
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

    override suspend fun userSignIn(request: UserSignInBody): ApiResponseState<AuthSignInResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.userSignIn(request)
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

    override suspend fun userForgotPassword(request: ForgotPasswordBody): ApiResponseState<ForgotPasswordResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.forgotPassword(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResponseState.Success(it)
                    } ?: ApiResponseState.Error("Response body is null")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                        jsonObject.get("errors")?.asString ?: "Unknown error"
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

    override suspend fun userVerifyOtp(request: VerifyOTPBody): ApiResponseState<ForgotPasswordResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.verifyOtp(request)
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