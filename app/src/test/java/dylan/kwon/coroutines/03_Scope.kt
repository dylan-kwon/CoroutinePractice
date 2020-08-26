package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

@Suppress("ClassName")
class `03_Scope` {

    @Test
    fun main() = runBlocking {
        measureTimeMillis {
            coroutineScope {
                launch {
                    println("1: ${job(1000)}")
                }
                launch {
                    println("2: ${job(2000)}")
                }
            }
            withContext(Dispatchers.IO) {
                launch {
                    println("3: ${job(1000)}")
                    launch {
                        println("4: ${job(1000)}")
                    }
                }
            }
            launch {
                println("5: ${job(1000)}")
            }.join()

            CoroutineScope(Dispatchers.IO).launch {
                println("6: ${job(5000)}")
            }
            GlobalScope.launch {
                println("7: ${job(5000)}")
            }
        }.let {
            println(it)
        }
    }

    private suspend fun job(millis: Long = 0): Boolean {
        delay(millis)
        return true
    }

}