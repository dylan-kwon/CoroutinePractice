package dylan.kwon.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

@Suppress("ClassName")
class `04_Channel` {

    private val channel: Channel<Int> by lazy {
        Channel<Int>()
    }

    @Test
    fun main() {
        runBlocking {
            launch(Dispatchers.IO) {
                repeat(5) {
                    delay(1000)
                    println("send: $it")
                    channel.send(it)
                }
                channel.close()
            }
            for (i in channel) {
                println("receive: $i")
            }
        }
        println("end.")
    }

}