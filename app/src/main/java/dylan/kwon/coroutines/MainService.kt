package dylan.kwon.coroutines

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()
        lifecycleScope.launch { /* ... */ }
    }

}

