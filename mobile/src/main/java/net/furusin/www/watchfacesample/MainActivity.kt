package net.furusin.www.watchfacesample

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private val TAG = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "battery Level = ${getBatteryLevel()}")
    }

    private fun getBatteryLevel(): Int? {
        return registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))?.let { batteryInfo ->
            val max = batteryInfo.getIntExtra(BatteryManager.EXTRA_SCALE, -1).toDouble()
            val level = batteryInfo.getIntExtra(BatteryManager.EXTRA_LEVEL, -1).toDouble()

            return@let (level / max * 100).toInt()
        }
    }
}
