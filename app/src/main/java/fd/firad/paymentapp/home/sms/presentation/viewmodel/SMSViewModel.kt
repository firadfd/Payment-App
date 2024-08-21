package fd.firad.paymentapp.home.sms.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.BaseViewModel
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import fd.firad.paymentapp.home.sms.data.model.AllSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSMSResponse
import fd.firad.paymentapp.home.sms.data.model.PaymentSendSmsBody
import fd.firad.paymentapp.home.sms.data.model.UpdateSMSStatusResponse
import fd.firad.paymentapp.home.sms.data.model.UpdateStatusBody
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
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

    fun userInfo() {
        viewModelScope.launch {
            _userInfoState.value = ApiResponseState.Loading
            try {
                if (getToken() != null) {
                    val result = smsUseCase.userInfo("Bearer ${getToken()!!}")
                    _userInfoState.value = result
                } else {
                    _userInfoState.value = ApiResponseState.Error("Token is null")
                }
            } catch (e: Exception) {
                _userInfoState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }


}