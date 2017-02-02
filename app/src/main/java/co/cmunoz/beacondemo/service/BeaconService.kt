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
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import com.hugeinc.sxsw.model.UserRequest
import com.hugeinc.sxsw.rest.ApiClient
import com.hugeinc.sxsw.rest.ApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Beacon Service
 *
 * User: cmunoz
 * Date: 1/24/17
 * Time: 2:57 PM
 */
class BeaconService : Service() {

  companion object {
    val TAG: String = "Beacon-Service"
    var BEACON_REGION = "B9407F30-F5F8-466E-AFF9-25556B57FE6D"
    var REGION_ID = "candy_beacon_xxx" //name defined in estimote cloud account
    val BEACON_MAJOR = 0 //major from estimote cloud account beacon info
    val BEACON_MINOR = 0 //minor from estimote cloud account beacon info
  }

  private lateinit var beaconManager: BeaconManager

  val service: ApiInterface = ApiClient().build().create(ApiInterface::class.java)
  lateinit var disposable: Disposable

  override fun onCreate() {
    super.onCreate()
  }

  override fun onBind(arg0: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Log.d(TAG, "Beacon Service Started")
    beaconManager = BeaconManager(applicationContext)

    beaconManager.setMonitoringListener(object : BeaconManager.MonitoringListener {
      override fun onEnteredRegion(region: Region, list: List<Beacon>) {
        disposable = service.beaconIn(UserRequest("email@email.com")).subscribeOn(
            Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe { userResponse ->
          showNotification("Beacon IN: ", "Open the gate status: " + userResponse.status)
          Log.d(TAG, "=========================================> Beacon IN: " + userResponse.status)
        }
      }

      override fun onExitedRegion(region: Region) {
        disposable = service.beaconOut(UserRequest("email@email.com")).subscribeOn(
            Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe { userResponse ->
          showNotification("Beacon OUT: ", "Bye bye status: " + userResponse.status)
          Log.d(TAG, "========================================> Beacon OUT: " + userResponse.status)
        }
      }
    })

    beaconManager.connect {
      beaconManager.startMonitoring(
          Region(REGION_ID, UUID.fromString(BEACON_REGION), BEACON_MAJOR, BEACON_MINOR))
    }

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
    disposable.dispose()
    beaconManager.disconnect()
    super.onDestroy()
  }
}