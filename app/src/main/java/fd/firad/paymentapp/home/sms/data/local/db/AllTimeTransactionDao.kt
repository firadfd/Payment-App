package fd.firad.paymentapp.home.sms.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fd.firad.paymentapp.home.sms.data.model.AllTimeTransactionResponse

@Dao
interface AllTimeTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTimeTransaction(allTimeTransaction: AllTimeTransactionResponse)

    @Query("SELECT * FROM all_time_info LIMIT 1")
    suspend fun getAllTimeTransaction(): AllTimeTransactionResponse?

    @Query("DELETE FROM all_time_info")
    suspend fun clearAllTimeTransaction()
}