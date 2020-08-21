package dylan.kwon.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import org.junit.Test

@Suppress("ClassName")
@ExperimentalCoroutinesApi
class `08_Select` {

    @Test
    fun main() {
        runBlocking {
            val fizz = createProduce(300, "Fizz")
            val buzz = createProduce(500, "Buzz")

            repeat(7) {
                selectProduces(fizz, buzz)
            }
            coroutineContext.cancelChildren()
        }
        println("end.")
    }

    private fun CoroutineScope.createProduce(delay: Long, data: String) = produce {
        delay(delay)
        send(data)
    }

    private suspend fun selectProduces(vararg produces: ReceiveChannel<String>) {
        select<Unit> {
            for (produce in produces) {
                produce.onReceive {
                    println("receive: $it")
                }
            }
        }
    }

}