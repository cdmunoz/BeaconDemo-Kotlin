package co.cmunoz.beacondemo

import android.app.Application
import com.estimote.sdk.EstimoteSDK

class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    EstimoteSDK.initialize(applicationContext, APP_ID, APP_TOKEN)
    EstimoteSDK.enableDebugLogging(true)

  }

  companion object {

    val APP_ID = "<API_ID_from_estimote_cloud>"
    val APP_TOKEN = "<API_TOKEN_from_estimote_cloud>"
  }
}
