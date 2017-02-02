package co.cmunoz.beacondemo.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Broadcast receiver
 *
 * User: cmunoz
 * Date: 1/24/17
 * Time: 3:58 PM
 */
class BeaconBroadcast: BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    context.startService(Intent(context, BeaconService::class.java))
  }
}