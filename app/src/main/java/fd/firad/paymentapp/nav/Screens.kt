package fd.firad.paymentapp.nav

import kotlinx.serialization.Serializable

@Serializable
object ScreenLogin

@Serializable
object ScreenRegistration

@Serializable
data class ScreenSendSms(val phone: String)