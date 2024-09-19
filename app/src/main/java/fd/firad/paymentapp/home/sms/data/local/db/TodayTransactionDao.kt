package fd.firad.paymentapp.home.sms.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse

@Dao
interface TodayTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodayTransaction(todayTransaction: TodayTransactionResponse)

    @Query("SELECT * FROM today_info LIMIT 1")
    suspend fun getTodayTransaction(): TodayTransactionResponse?

    @Query("DELETE FROM today_info")
    suspend fun clearTodayTransaction()
}