package fd.firad.paymentapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fd.firad.paymentapp.home.sms.data.local.db.AllTimeTransactionDao
import fd.firad.paymentapp.home.sms.data.local.db.MonthlyTransactionDao
import fd.firad.paymentapp.home.sms.data.local.db.TodayTransactionDao
import fd.firad.paymentapp.home.sms.data.local.db.UserInfoDao
import fd.firad.paymentapp.home.sms.data.local.db.WeeklyTransactionDao
import fd.firad.paymentapp.home.sms.data.local.db.YearlyTransactionDao
import fd.firad.paymentapp.home.sms.data.local.model.Converters
import fd.firad.paymentapp.home.sms.data.model.AllTimeTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.MonthlyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.TodayTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.UserInfoResponse
import fd.firad.paymentapp.home.sms.data.model.WeeklyTransactionResponse
import fd.firad.paymentapp.home.sms.data.model.YearlyTransactionResponse
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.entity.SmsEntity

@Database(
    entities = [
        SmsEntity::class,
        UserInfoResponse::class,
        TodayTransactionResponse::class,
        WeeklyTransactionResponse::class,
        MonthlyTransactionResponse::class,
        YearlyTransactionResponse::class,
        AllTimeTransactionResponse::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SmsDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun userInfoDao(): UserInfoDao
    abstract fun todayTransactionDao(): TodayTransactionDao
    abstract fun weeklyTransactionDao(): WeeklyTransactionDao
    abstract fun monthlyTransactionDao(): MonthlyTransactionDao
    abstract fun yearlyTransactionDao(): YearlyTransactionDao
    abstract fun allTimeTransactionDao(): AllTimeTransactionDao

}