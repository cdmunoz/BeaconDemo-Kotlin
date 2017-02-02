package co.cmunoz.beacondemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.cmunoz.beacondemo.service.BeaconService
import com.estimote.sdk.SystemRequirementsChecker

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    startService(Intent(this, BeaconService::class.java))
  }

  override fun onResume() {
    super.onResume()
    SystemRequirementsChecker.checkWithDefaultDialogs(this)
    //registerReceiver(BeaconBroadcast(), null)
  }

  override fun onPause() {
    //unregisterReceiver(BeaconBroadcast())
    super.onPause()
  }
}
