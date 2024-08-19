package fd.firad.paymentapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.entity.SmsEntity

@Database(entities = [SmsEntity::class], version = 1)
abstract class SmsDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
}