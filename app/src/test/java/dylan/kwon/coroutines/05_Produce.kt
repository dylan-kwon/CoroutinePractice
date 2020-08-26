package dylan.kwon.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

@Suppress("ClassName")
@ExperimentalCoroutinesApi
class `05_Produce` {

    @Test
    fun main() = runBlocking {
        createProduce().consumeEach {
            println("receive: $it")
        }
    }

    private fun CoroutineScope.createProduce() = produce {
        for (i in 0 until 5) {
            println("send: $i")
            delay(500)
            send(i)
        }
    }

}