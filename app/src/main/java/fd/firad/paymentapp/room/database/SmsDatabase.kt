package fd.firad.paymentapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fd.firad.paymentapp.home.sms.data.local.db.UserInfoDao
import fd.firad.paymentapp.home.sms.data.local.model.Converters
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.entity.SmsEntity

@Database(entities = [SmsEntity::class, UserInfoResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class SmsDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun userInfoDao(): UserInfoDao
}