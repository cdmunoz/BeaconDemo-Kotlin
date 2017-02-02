package co.cmunoz.beacondemo.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import co.cmunoz.beacondemo.MainActivity
import com.estimote.sdk.BeaconManager
import io.reactivex.disposables.Disposable


/**
 * Nearable Service
 *
 * User: cmunoz
 * Date: 1/24/17
 * Time: 2:57 PM
 */
class NearableService : Service() {

  companion object {
    val TAG: String = "Nearable-Service"
    var NEARABLE_REGION = "D0D3FA86-CA76-45EC-9BD9-6AF49D970425"
    var NEARABLE_REGION_ID = "shoe"
  }

  private lateinit var nearableManager: BeaconManager
  private lateinit var scanId: String
  lateinit var disposable: Disposable

  override fun onCreate() {
    super.onCreate()
    nearableManager = BeaconManager(applicationContext)
    nearableManager.setBackgroundScanPeriod(200, 0)
    nearableManager.setForegroundScanPeriod(200, 0)

    /*nearableManager.setNearableListener {
      BeaconManager.NearableListener { nearables ->
        for (nearable: Nearable in nearables) {
          Log.e(TAG,
              "++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearables: " + nearable.identifier)
        }
      }
    }*/
    nearableManager.setEddystoneListener { BeaconManager.EddystoneListener { nearables -> Log.e(TAG,
        "++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearables: " + nearables) } }
  }

  override fun onBind(arg0: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Log.e(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearable Service Started")

    nearableManager.connect({
//      scanId = nearableManager.startNearableDiscovery()
      scanId = nearableManager.startEddystoneScanning()
      Log.e(TAG, "++++++++++++++++++++++++++++++++++++++++++++++++++++++> scanId: " + scanId)
    })

    nearableManager.setErrorListener { Log.e(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++> Nearable Error! ") }

    return START_REDELIVER_INTENT
  }

  fun showNotification(title: String, message: String) {
    val notifyIntent = Intent(this, MainActivity::class.java)
    notifyIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    val pendingIntent = PendingIntent.getActivities(this, 0,
        arrayOf(notifyIntent), PendingIntent.FLAG_UPDATE_CURRENT)
    val notification = Notification.Builder(this)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()
    notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1, notification)
  }

  override fun onDestroy() {
    Log.d(TAG, "Beacon Service Destroyed")
    if (null != disposable) disposable.dispose()
    nearableManager.stopNearableDiscovery(scanId)
    nearableManager.disconnect()
    super.onDestroy()
  }
}