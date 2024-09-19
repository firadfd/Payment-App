package fd.firad.paymentapp.home.sms.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.BaseViewModel
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.model.convertAllTimeToTodayTransaction
import fd.firad.paymentapp.home.sms.data.model.convertMonthlyToTodayTransaction
import fd.firad.paymentapp.home.sms.data.model.convertWeeklyToTodayTransaction
import fd.firad.paymentapp.home.sms.data.model.convertYearlyToTodayTransaction
import fd.firad.paymentapp.home.sms.domain.usecase.SMSUseCase
import fd.firad.paymentapp.room.entity.SmsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SMSViewModel @Inject constructor(
    private val smsUseCase: SMSUseCase, sharedPreferenceManager: SharedPreferenceManager
) : BaseViewModel(sharedPreferenceManager) {


    private val _failedSmsList = MutableStateFlow<List<SmsEntity>>(emptyList())
    val failedSmsList: StateFlow<List<SmsEntity>> get() = _failedSmsList

    fun loadFailedSms() {
        viewModelScope.launch {
            smsUseCase.getFailedSms().observeForever { smsList ->
                _failedSmsList.value = smsList ?: emptyList()
            }
        }
    }

    fun insertSms(smsEntity: SmsEntity) {
        viewModelScope.launch {
            smsUseCase.insertSms(smsEntity)
        }
    }

    fun deleteSms(smsEntity: SmsEntity) {
        viewModelScope.launch {
            smsUseCase.deleteSms(smsEntity)
        }
    }

    private val _allSMSState =
        MutableStateFlow<ApiResponseState<AllSMSResponse>>(ApiResponseState.Loading)
    val allSMSState: StateFlow<ApiResponseState<AllSMSResponse>> = _allSMSState

    fun allSMS(
        apiKey: String, secretKey: String
    ) {
        viewModelScope.launch {
            _allSMSState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.allSms("Bearer ${getToken()!!}", apiKey, secretKey)
                    _allSMSState.value = result
                } else {
                    _allSMSState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _allSMSState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    private val _pendingSMSState =
        MutableStateFlow<ApiResponseState<AllSMSResponse>>(ApiResponseState.Loading)
    val pendingSMSState: StateFlow<ApiResponseState<AllSMSResponse>> = _pendingSMSState

    fun pendingSMS(
        apiKey: String, secretKey: String
    ) {
        viewModelScope.launch {
            _pendingSMSState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.pendingSms("Bearer ${getToken()!!}", apiKey, secretKey)
                    _pendingSMSState.value = result
                } else {
                    _pendingSMSState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _pendingSMSState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }


    private val _updateSMSStatusState =
        MutableStateFlow<ApiResponseState<UpdateSMSStatusResponse>>(ApiResponseState.Loading)
    val updateSMSStatusState: StateFlow<ApiResponseState<UpdateSMSStatusResponse>> =
        _updateSMSStatusState

    fun updateSMSStatus(apiKey: String, secretKey: String, id: Int, request: UpdateStatusBody) {
        viewModelScope.launch {
            _updateSMSStatusState.value = ApiResponseState.Loading

            try {
                if (getToken() != null) {
                    val result = smsUseCase.updateSmsStatus(
                        "Bearer ${getToken()!!}",
                        id = id,
                        apiKey = apiKey,
                        secretKey = secretKey,
                        request = request
                    )
                    _updateSMSStatusState.value = result
                } else {
                    _updateSMSStatusState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _updateSMSStatusState.value =
                    ApiResponseState.Error("An error occurred: ${e.message}")

            }
        }
    }

    private val _paymentSMSState =
        MutableStateFlow<ApiResponseState<PaymentSMSResponse>>(ApiResponseState.Loading)
    val paymentSMSState: StateFlow<ApiResponseState<PaymentSMSResponse>> = _paymentSMSState

    fun paymentSms(
        apiKey: String, secretKey: String, request: PaymentSendSmsBody
    ) {
        viewModelScope.launch {
            _paymentSMSState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result =
                        smsUseCase.paymentSms("Bearer ${getToken()!!}", apiKey, secretKey, request)
                    _paymentSMSState.value = result
                } else {
                    _paymentSMSState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _paymentSMSState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    private val _userInfoState =
        MutableStateFlow<ApiResponseState<UserInfoResponse>>(ApiResponseState.Loading)
    val userInfoState: StateFlow<ApiResponseState<UserInfoResponse>> = _userInfoState

    fun userInfo(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _userInfoState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.userInfo("Bearer ${getToken()!!}", forceFetch)
                    _userInfoState.value = result
                } else {
                    _userInfoState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _userInfoState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }


    private val _transactionState =
        MutableStateFlow<ApiResponseState<TodayTransactionResponse>>(ApiResponseState.Loading)
    val transactionState: StateFlow<ApiResponseState<TodayTransactionResponse>> = _transactionState

    fun todayTransaction(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _transactionState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.todayTransaction("Bearer ${getToken()}",forceFetch)
                    Log.e("TAG", "$result")
                    _transactionState.value = result
                } else {
                    _transactionState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _transactionState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun weeklyTransaction(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _transactionState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.weeklyTransaction("Bearer ${getToken()}",forceFetch)
                    _transactionState.value = when (result) {
                        is ApiResponseState.Success -> {
                            val todayTransaction = convertWeeklyToTodayTransaction(result.data)
                            Log.e("TAG", "$todayTransaction" )
                            ApiResponseState.Success(todayTransaction)
                        }
                        is ApiResponseState.Error -> {
                            ApiResponseState.Error(result.errorMessage)
                        }
                        else -> ApiResponseState.Error("Unknown error occurred")
                    }
                } else {
                    _transactionState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _transactionState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun monthlyTransaction(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _transactionState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.monthlyTransaction("Bearer ${getToken()}",forceFetch)
                    _transactionState.value = when (result) {
                        is ApiResponseState.Success -> {
                            val todayTransaction = convertMonthlyToTodayTransaction(result.data)
                            ApiResponseState.Success(todayTransaction)
                        }
                        is ApiResponseState.Error -> {
                            ApiResponseState.Error(result.errorMessage)
                        }
                        else -> ApiResponseState.Error("Unknown error occurred")
                    }
                } else {
                    _transactionState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _transactionState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun yearlyTransaction(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _transactionState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.yearlyTransaction("Bearer ${getToken()}",forceFetch)
                    _transactionState.value = when (result) {
                        is ApiResponseState.Success -> {
                            val todayTransaction = convertYearlyToTodayTransaction(result.data)
                            ApiResponseState.Success(todayTransaction)
                        }
                        is ApiResponseState.Error -> {
                            ApiResponseState.Error(result.errorMessage)
                        }
                        else -> ApiResponseState.Error("Unknown error occurred")
                    }
                } else {
                    _transactionState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _transactionState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun allTimeTransaction(forceFetch: Boolean = false) {
        viewModelScope.launch {
            _transactionState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.allTimeTransaction("Bearer ${getToken()}",forceFetch)
                    _transactionState.value = when (result) {
                        is ApiResponseState.Success -> {
                            val todayTransaction = convertAllTimeToTodayTransaction(result.data)
                            ApiResponseState.Success(todayTransaction)
                        }
                        is ApiResponseState.Error -> {
                            ApiResponseState.Error(result.errorMessage)
                        }
                        else -> ApiResponseState.Error("Unknown error occurred")
                    }
                } else {
                    _transactionState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _transactionState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }


}