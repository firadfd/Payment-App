package fd.firad.paymentapp.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fd.firad.paymentapp.auth.data.api.AuthApiService
import fd.firad.paymentapp.auth.data.repository.AuthRepositoryImpl
import fd.firad.paymentapp.auth.domain.repository.AuthRepository
import fd.firad.paymentapp.common.constants.Constants.BASE_URL
import fd.firad.paymentapp.home.sms.data.local.db.UserInfoDao
import fd.firad.paymentapp.home.sms.data.remote.SMSApiService
import fd.firad.paymentapp.home.sms.data.repository.SMSRepositoryImpl
import fd.firad.paymentapp.home.sms.domain.repository.SMSRepository
import fd.firad.paymentapp.room.dao.SmsDao
import fd.firad.paymentapp.room.database.SmsDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun authApiService(httpClient: OkHttpClient, gson: Gson): AuthApiService =
        Retrofit.Builder().run {
            baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
        }.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun smsApiService(httpClient: OkHttpClient, gson: Gson): SMSApiService =
        Retrofit.Builder().run {
            baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
        }.create(SMSApiService::class.java)


    @Provides
    @Singleton
    fun provideAuthRepository(apiService: AuthApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSMSRepository(
        apiService: SMSApiService,
        smsDao: SmsDao,
        userInfoDao: UserInfoDao,
        @ApplicationContext context: Context,
        connectivityManager: ConnectivityManager
    ): SMSRepository {
        return SMSRepositoryImpl(apiService, smsDao, userInfoDao, context, connectivityManager)
    }

    @Provides
    @Singleton
    fun buildGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext context: Context): SmsDatabase {
        return Room.databaseBuilder(context, SmsDatabase::class.java, "payment_sms_db").build()
    }

    @Provides
    fun provideSmsDao(database: SmsDatabase): SmsDao {
        return database.smsDao()
    }

    @Provides
    fun provideUserInfoDao(appDatabase: SmsDatabase): UserInfoDao {
        return appDatabase.userInfoDao()
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}