package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

@Suppress("ClassName")
class `02_Async` {

    @Test
    fun main() {
        runBlocking {
            measureTimeMillis {
                val deferred1 = async {
                    job(1000)
                }
                val deferred2 = async {
                    job(2000)
                }
                val deferred3 = async(Dispatchers.IO, start = CoroutineStart.LAZY) {
                    job(3000)
                }
                println("1: ${deferred1.await()}")
                println("2: ${deferred2.await()}")

                deferred3.start()
                println("3: ${deferred3.await()}")
            }.let {
                println(it)
            }
        }
        println("end.")
    }

    private suspend fun job(millis: Long): Boolean {
        delay(millis)
        return true
    }
}