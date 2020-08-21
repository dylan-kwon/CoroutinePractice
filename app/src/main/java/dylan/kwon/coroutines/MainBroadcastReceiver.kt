package dylan.kwon.coroutines

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainBroadcastReceiver : LifecycleBroadcastReceiver() {

    companion object {
        const val TAG: String = "MainBroadcastReceiver"
        const val INTENT_ACTION: String = "dylan.kwon.coroutine.BROAD_CAST_ACTION"
    }

    override val intentFilter: IntentFilter by lazy {
        IntentFilter(INTENT_ACTION)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d(TAG, "onReceived: ${intent?.getIntExtra("cnt", -1)}")
        }
    }

}

