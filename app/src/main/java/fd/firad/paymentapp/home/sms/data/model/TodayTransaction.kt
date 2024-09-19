package fd.firad.paymentapp.home.sms.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "today_info")
data class TodayTransactionResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val status: String,
    val message: String? = null,
    val todayTransaction: String,
    val bkash: String,
    val nagad: String,
    val rocket: String,
    val upay: String,
)