package fd.firad.paymentapp.home.sms.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fd.firad.paymentapp.home.sms.data.model.WeeklyTransactionResponse

@Dao
interface WeeklyTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyTransaction(weeklyTransaction: WeeklyTransactionResponse)

    @Query("SELECT * FROM weekly_info LIMIT 1")
    suspend fun getWeeklyTransaction(): WeeklyTransactionResponse?

    @Query("DELETE FROM weekly_info")
    suspend fun clearWeeklyTransaction()
}