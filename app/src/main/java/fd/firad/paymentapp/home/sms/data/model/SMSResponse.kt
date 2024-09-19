package fd.firad.paymentapp.home.sms.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SendSMSResponse(
    val status: String, val message: String, val data: SendSMSData
)

data class SendSMSData(
    val number: String,
    val message: String,
    val updated_at: String,
    val created_at: String,
    val user_id: Int,
    val id: Int
)


data class AllSMSResponse(
    val status: String, val message: String, val data: List<AllSMSData>
)

data class AllSMSData(
    val id: Int,
    val user_id: Int,
    val number: String,
    val message: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
)

data class UpdateSMSStatusResponse(
    val status: String, val message: String, val data: AllSMSData
)

data class PaymentSMSResponse(
    val status: Boolean, val message: String, val data: PaymentSMSData
)

@Entity(tableName = "user_info")
data class UserInfoResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val status: Boolean,
    val message: String,
    val totalSmsCount: Int,
    val pendingsmscount: Int,
    val sentsmscount: Int,
    val faildsmscount: Int,
    val totalpaymentcount: Int,
    val pendingpayment: Int,
    val successpayment: Int,
    val cancalledpayment: Int,
    val user: UserInfoData,
    val subscriptions: SubscriptionsResponse
)

data class UserInfoData(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val device_id: String? = null,
    val status: String,
    val role: String,
    val profile_image: String? = null,
    val email_verified_at: String? = null,
    val created_at: String,
    val updated_at: String,
)

data class SubscriptionsResponse(
    val id: Int,
    val user_id: Int,
    val subscription_type: String? = null,
    val price: Int,
    val limit: String,
    val start_date: String,
    val end_date: String,
    val created_at: String,
    val updated_at: String,
    val api: List<ApiResponse>
)

data class ApiResponse(
    val id: Int,
    val user_id: Int,
    val subscription_id: Int,
    val api_key: String? = null,
    val secret_key: String? = null,
    val status: String,
    val created_at: String,
    val updated_at: String,
)

data class PaymentSMSData(
    val sender: String,
    val sms_text: String,
    val user_id: String,
    val updated_at: String,
    val created_at: String,
    val id: Int,
)


@Entity(tableName = "weekly_info")
data class WeeklyTransactionResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val status: String,
    val message: String? = null,
    val weeklyTransaction: String,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)

@Entity(tableName = "monthly_info")
data class MonthlyTransactionResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val status: String,
    val message: String? = null,
    val monthlyTransaction: String,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)

@Entity(tableName = "yearly_info")
data class YearlyTransactionResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val status: String,
    val message: String? = null,
    val yearlyTransaction: String,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)

@Entity(tableName = "all_time_info")
data class AllTimeTransactionResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val status: String,
    val message: String? = null,
    val allTimeTransaction: String,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)

// Function to map any transaction response to TodayTransactionResponse
fun mapToTodayTransaction(
    status: String,
    message: String,
    todayTransaction: String,
    bkash: String,
    nagad: String,
    rocket: String,
    upay: String
): TodayTransactionResponse {
    return TodayTransactionResponse(
        status = status,
        message = message,
        todayTransaction = todayTransaction,
        bkash = bkash,
        nagad = nagad,
        rocket = rocket,
        upay = upay
    )
}


fun convertWeeklyToTodayTransaction(weeklyResponse: WeeklyTransactionResponse): TodayTransactionResponse {
    return mapToTodayTransaction(
        status = weeklyResponse.status,
        message = weeklyResponse.message ?: "",
        todayTransaction =  weeklyResponse.weeklyTransaction,
        bkash = weeklyResponse.bkash,
        nagad = weeklyResponse.nagad,
        rocket = weeklyResponse.rocket,
        upay = weeklyResponse.upay
    )
}

fun convertMonthlyToTodayTransaction(monthlyResponse: MonthlyTransactionResponse): TodayTransactionResponse {
    return mapToTodayTransaction(
        status = monthlyResponse.status,
        message = monthlyResponse.message ?: "",
        todayTransaction = monthlyResponse.monthlyTransaction,
        bkash = monthlyResponse.bkash,
        nagad = monthlyResponse.nagad,
        rocket = monthlyResponse.rocket,
        upay = monthlyResponse.upay
    )
}


fun convertYearlyToTodayTransaction(yearlyResponse: YearlyTransactionResponse): TodayTransactionResponse {
    return mapToTodayTransaction(
        status = yearlyResponse.status,
        message = yearlyResponse.message ?: "",
        todayTransaction = yearlyResponse.yearlyTransaction,
        bkash = yearlyResponse.bkash,
        nagad = yearlyResponse.nagad,
        rocket = yearlyResponse.rocket,
        upay = yearlyResponse.upay
    )
}


fun convertAllTimeToTodayTransaction(allTimeResponse: AllTimeTransactionResponse): TodayTransactionResponse {
    return mapToTodayTransaction(
        status = allTimeResponse.status,
        message = allTimeResponse.message ?: "",
        todayTransaction = allTimeResponse.allTimeTransaction,
        bkash = allTimeResponse.bkash,
        nagad = allTimeResponse.nagad,
        rocket = allTimeResponse.rocket,
        upay = allTimeResponse.upay
    )
}





