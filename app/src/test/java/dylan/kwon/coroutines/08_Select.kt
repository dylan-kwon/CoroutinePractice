package dylan.kwon.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Suppress("ClassName")
class `08_Select` {

    @Test
    fun main() = runBlocking {
        val channel1 = produce {
            for (i in 0 until 7) {
                delay(1000)
                send("channel1")
            }
        }
        val channel2 = produce {
            for (i in 0 until 5) {
                delay(2000)
                send("channel2")
            }
        }
        while (true) select<String?> {
            channel1.onReceiveOrClosed {
                if (it.isClosed) null else it.valueOrNull
            }
            channel2.onReceiveOrClosed {
                if (it.isClosed) null else it.valueOrNull
            }
        }?.let {
            println(it)
        }
    }


}