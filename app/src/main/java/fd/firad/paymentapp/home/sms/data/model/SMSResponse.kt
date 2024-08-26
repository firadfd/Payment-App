package fd.firad.paymentapp.home.sms.data.model

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

data class UserInfoResponse(
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

data class TransactionResponse(
    val status: String,
    val message: String,
    val todayTransaction: String? = null,
    val weeklyTransaction: String? = null,
    val monthlyTransaction: String? = null,
    val yearlyTransaction: String? = null,
    val allTimeTransaction: String? = null,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)
