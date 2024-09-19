package fd.firad.paymentapp.home.sms.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fd.firad.paymentapp.home.sms.data.model.YearlyTransactionResponse

@Dao
interface YearlyTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYearlyTransaction(yearlyTransaction: YearlyTransactionResponse)

    @Query("SELECT * FROM yearly_info LIMIT 1")
    suspend fun getYearlyTransaction(): YearlyTransactionResponse?

    @Query("DELETE FROM yearly_info")
    suspend fun clearYearlyTransaction()
}