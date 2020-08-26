package dylan.kwon.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.system.measureTimeMillis

@Suppress("ClassName")
class `09_Mutex` {

    private var count: Int = 0

    private val mutex: Mutex by lazy { Mutex() }

    @Test
    fun main() = runBlocking {
        work {
            mutex.withLock { count++ }
        }
        println("count: $count")
    }

    private suspend fun work(action: suspend () -> Unit) = withContext(Dispatchers.IO) {
        measureTimeMillis {
            val jobs = List(100) {
                launch {
                    repeat(1000) { action() }
                }
            }
            jobs.forEach { it.join() }

        }.let {
            println("time: $it")
        }
    }

}