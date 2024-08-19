package fd.firad.paymentapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RootApp : Application()
//
//    , Configuration.Provider {
//
//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory
//
//    override val workManagerConfiguration: Configuration
//        get() = Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//}