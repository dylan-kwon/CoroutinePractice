package dylan.kwon.coroutines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val receiver: MainBroadcastReceiver by lazy {
        MainBroadcastReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiver.register(applicationContext)
        lifecycleScope.launch { /* ... */ }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO) {
            var cnt = 0
            while (true) {
                delay(2000)
                Log.d("LifecycleBroadcast", "broadcast.")
                sendBroadcast(Intent(MainBroadcastReceiver.INTENT_ACTION).apply {
                    putExtra("cnt", cnt++)
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.unregister(applicationContext)
    }

}