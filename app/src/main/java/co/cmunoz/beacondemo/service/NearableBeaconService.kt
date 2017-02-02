package co.cmunoz.beacondemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import io.reactivex.disposables.Disposable
import java.util.*


/**
 * Nearable Beacon Service
 *
 * User: cmunoz
 * Date: 1/24/17
 * Time: 2:57 PM
 */
class NearableBeaconService : Service() {

  companion object {
    val TAG: String = "Nearable-Service"
    var NEARABLE_REGION = "D0D3FA86-CA76-45EC-9BD9-6AF49D970425"
    var NEARABLE_REGION_ID = "shoe" //name defined in estimote cloud account
    val NEARABLE_MINOR = 40587 //major from estimote cloud account nearable info
    val NEARABLE_MAJOR = 44166 //minor from estimote cloud account nearable info
  }

  private lateinit var nearableManager: BeaconManager
  private lateinit var scanId: String
  lateinit var disposable: Disposable

  override fun onCreate() {
    super.onCreate()
  }

  override fun onBind(arg0: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Log.e(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearable Service Started")

    nearableManager = BeaconManager(applicationContext)

    nearableManager.setMonitoringListener(object : BeaconManager.MonitoringListener {
      override fun onExitedRegion(region: Region) {
      }

      override fun onEnteredRegion(region: Region, nearables: List<Beacon>) {
        for (nearable in nearables) {
          Log.e(TAG,
              "++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearables: " + nearable.macAddress)
        }
      }
    })

    nearableManager.connect({
      nearableManager.startMonitoring(
          Region(NEARABLE_REGION_ID, UUID.fromString(NEARABLE_REGION), NEARABLE_MAJOR,
              NEARABLE_MINOR))
    })

    return START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    Log.d(TAG, "Nearable Service Destroyed")
    if (null != disposable) disposable.dispose()
    nearableManager.disconnect()
    super.onDestroy()
  }
}