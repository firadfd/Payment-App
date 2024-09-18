package fd.firad.paymentapp.home.sms.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fd.firad.paymentapp.home.sms.data.model.ApiResponse
import fd.firad.paymentapp.home.sms.data.model.SubscriptionsResponse
import fd.firad.paymentapp.home.sms.data.model.UserInfoData

class Converters {

    // Convert UserInfoData to and from String (JSON)
    @TypeConverter
    fun fromUserInfoData(userInfoData: UserInfoData?): String? {
        return Gson().toJson(userInfoData)
    }

    @TypeConverter
    fun toUserInfoData(userInfoDataString: String?): UserInfoData? {
        return if (userInfoDataString == null) {
            null
        } else {
            val type = object : TypeToken<UserInfoData>() {}.type
            Gson().fromJson(userInfoDataString, type)
        }
    }

    // Convert SubscriptionsResponse to and from String (JSON)
    @TypeConverter
    fun fromSubscriptionsResponse(subscriptionsResponse: SubscriptionsResponse?): String? {
        return Gson().toJson(subscriptionsResponse)
    }

    @TypeConverter
    fun toSubscriptionsResponse(subscriptionsResponseString: String?): SubscriptionsResponse? {
        return if (subscriptionsResponseString == null) {
            null
        } else {
            val type = object : TypeToken<SubscriptionsResponse>() {}.type
            Gson().fromJson(subscriptionsResponseString, type)
        }
    }

    // Convert List<ApiResponse> to and from String (JSON)
    @TypeConverter
    fun fromApiResponseList(apiResponseList: List<ApiResponse>?): String? {
        return Gson().toJson(apiResponseList)
    }

    @TypeConverter
    fun toApiResponseList(apiResponseListString: String?): List<ApiResponse>? {
        return if (apiResponseListString == null) {
            null
        } else {
            val type = object : TypeToken<List<ApiResponse>>() {}.type
            Gson().fromJson(apiResponseListString, type)
        }
    }
}
