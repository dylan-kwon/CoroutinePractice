package dylan.kwon.coroutines

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import java.util.concurrent.atomic.AtomicReference

abstract class LifecycleBroadcastReceiver : BroadcastReceiver(), LifecycleOwner {

    companion object {
        const val TAG: String = "LifecycleBroadcast"
    }

    open val intentFilter: IntentFilter by lazy {
        IntentFilter()
    }

    private val lifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    open val isActive: Boolean
        get() = lifecycle.currentState == Lifecycle.State.CREATED ||
                lifecycle.currentState == Lifecycle.State.STARTED ||
                lifecycle.currentState == Lifecycle.State.RESUMED

    init {
        active()
    }

    @CallSuper
    open fun register(context: Context) {
        Log.d(TAG, "register.")
        active()
        context.registerReceiver(this, intentFilter)
    }

    @CallSuper
    open fun unregister(context: Context) {
        Log.d(TAG, "unregister.")
        inactive()
        context.unregisterReceiver(this)
    }

    private fun active() {
        if (isActive) return
        Log.d(TAG, "active.")
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        clearLifecycleScope()
        onActive()
    }

    private fun inactive() {
        if (!isActive) return
        Log.d(TAG, "inactive.")
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        onInactive()
    }

    @CallSuper
    protected open fun onActive() {
        Log.d(TAG, "onActive.")
    }

    @CallSuper
    protected open fun onInactive() {
        Log.d(TAG, "onInactive.")
    }

    private fun clearLifecycleScope() = try {
        val lifecycleClass = Lifecycle::class.java
        val mInternalScopeRef = lifecycleClass.getDeclaredField("mInternalScopeRef").apply {
            isAccessible = true
        }
        (mInternalScopeRef.get(lifecycle) as? AtomicReference<*>)?.set(null)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}