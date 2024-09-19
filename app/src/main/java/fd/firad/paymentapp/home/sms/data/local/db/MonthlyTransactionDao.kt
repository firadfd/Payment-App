package fd.firad.paymentapp.home.sms.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fd.firad.paymentapp.home.sms.data.model.MonthlyTransactionResponse

@Dao
interface MonthlyTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyTransaction(monthlyTransaction: MonthlyTransactionResponse)

    @Query("SELECT * FROM monthly_info LIMIT 1")
    suspend fun getMonthlyTransaction(): MonthlyTransactionResponse?

    @Query("DELETE FROM monthly_info")
    suspend fun clearMonthlyTransaction()
}