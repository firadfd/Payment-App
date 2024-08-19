package fd.firad.paymentapp.common.presentation

import androidx.lifecycle.ViewModel
import fd.firad.paymentapp.common.utils.SharedPreferenceManager
import javax.inject.Inject

open class BaseViewModel @Inject constructor(
    private val sharedPreferenceManager: SharedPreferenceManager,
) : ViewModel() {

    fun getToken() = sharedPreferenceManager.getToken()
    fun getApiToken() = sharedPreferenceManager.getApi()
    fun getSecretKey() = sharedPreferenceManager.getSecret()

    fun saveToken(token: String) {
        sharedPreferenceManager.saveToken(token)
    }

    fun saveApiToken(token: String) {
        sharedPreferenceManager.saveApi(token)
    }

    fun saveSecretKey(token: String) {
        sharedPreferenceManager.saveSecret(token)

    }

    fun deleteToken() {
        sharedPreferenceManager.deleteToken()
    }

    fun deleteApiToken() {
        sharedPreferenceManager.deleteApi()
    }

    fun deleteSecretKey() {
        sharedPreferenceManager.deleteSecret()
    }

}