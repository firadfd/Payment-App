package fd.firad.paymentapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fd.firad.paymentapp.room.entity.SmsEntity

@Dao
interface SmsDao {
    @Insert
    suspend fun insertSms(smsEntity: SmsEntity)

    @Query("SELECT * FROM sms_table")
    fun getAllSms(): LiveData<List<SmsEntity>>

    @Delete
    suspend fun deleteSms(smsEntity: SmsEntity)
}