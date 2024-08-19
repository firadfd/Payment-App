package fd.firad.paymentapp.common.model

sealed class ApiResponseState<out T> {
    data class Success<out T>(val data: T) : ApiResponseState<T>()
    data class Error(val errorMessage: String) : ApiResponseState<Nothing>()
    object Loading : ApiResponseState<Nothing>()
}