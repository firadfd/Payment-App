package fd.firad.paymentapp.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fd.firad.paymentapp.auth.data.model.AuthSignInResponse
import fd.firad.paymentapp.auth.data.model.AuthSignUpResponse
import fd.firad.paymentapp.auth.data.model.UserSignInBody
import fd.firad.paymentapp.auth.data.model.UserSignUpBody
import fd.firad.paymentapp.auth.domain.usecase.UserAuthUseCase
import fd.firad.paymentapp.common.model.ApiResponseState
import fd.firad.paymentapp.common.presentation.BaseViewModel
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userLogInUseCase: UserAuthUseCase, sharedPreferenceManager: SharedPreferenceManager
) : BaseViewModel(sharedPreferenceManager) {

    //1
    private val _signUpState =
        MutableStateFlow<ApiResponseState<AuthSignUpResponse>>(ApiResponseState.Loading)
    val signUpState: StateFlow<ApiResponseState<AuthSignUpResponse>> = _signUpState

    fun userSignUp(request: UserSignUpBody) {
        viewModelScope.launch {
            _signUpState.value = ApiResponseState.Loading
            try {
                val result = userLogInUseCase.userSignup(request)
                _signUpState.value = result
            } catch (e: Exception) {
                _signUpState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }

    //2
    private val _signInState =
        MutableStateFlow<ApiResponseState<AuthSignInResponse>>(ApiResponseState.Loading)
    val signInState: StateFlow<ApiResponseState<AuthSignInResponse>> = _signInState

    fun userSignIn(request: UserSignInBody) {
        viewModelScope.launch {
            _signInState.value = ApiResponseState.Loading
            try {
                val result = userLogInUseCase.userSignIn(request)
                _signInState.value = result
            } catch (e: Exception) {
                _signInState.value = ApiResponseState.Error("An error occurred: ${e.message}")
            }
        }
    }
}
