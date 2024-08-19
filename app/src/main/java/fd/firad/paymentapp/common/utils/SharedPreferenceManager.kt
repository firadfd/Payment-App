package fd.firad.paymentapp.common.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("paymentApp", Context.MODE_PRIVATE)

    // Function to add or update token in SharedPreferences
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun saveApi(api: String) {
        sharedPreferences.edit().putString("api", api).apply()
    }

    fun saveSecret(secret: String) {
        sharedPreferences.edit().putString("secret", secret).apply()
    }

    // Function to update the "token" in SharedPreferences
    fun updateToken(newToken: String) {
        sharedPreferences.edit().putString("token", newToken).apply()
    }

    // Function to update the "api" in SharedPreferences
    fun updateApi(newApi: String) {
        sharedPreferences.edit().putString("api", newApi).apply()
    }

    // Function to update the "secret" in SharedPreferences
    fun updateSecret(newSecret: String) {
        sharedPreferences.edit().putString("secret", newSecret).apply()
    }

    // Function to retrieve token from SharedPreferences
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun getApi(): String? {
        return sharedPreferences.getString("api", null)
    }

    fun getSecret(): String? {
        return sharedPreferences.getString("secret", null)
    }

    // Function to delete token from SharedPreferences
    fun deleteToken() {
        sharedPreferences.edit().remove("token").apply()
    }

    fun deleteApi() {
        sharedPreferences.edit().remove("api").apply()
    }

    fun deleteSecret() {
        sharedPreferences.edit().remove("secret").apply()
    }
}
