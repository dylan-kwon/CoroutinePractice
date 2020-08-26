package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

@Suppress("ClassName")
class `01_Launch` {

    @Test
    fun main() = runBlocking {
        measureTimeMillis {
            launch {
                println("1: ${job(1000)}")
            }.join()

            launch {
                println("2: ${job(2000)}")
            }.join()

            val job = launch(Dispatchers.IO) {
                launch {
                    while (true) {
                        println("3: ${job(3000)}")
                    }
                }
            }
            delay(7000)
            job.cancelAndJoin()

        }.let {
            println(it)
        }
    }


    private suspend fun job(millis: Long): Boolean {
        delay(millis)
        return true
    }

}