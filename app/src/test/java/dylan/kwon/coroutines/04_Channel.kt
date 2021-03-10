package dylan.kwon.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@Suppress("ClassName")
class `04_Channel` {

    private val channel: BroadcastChannel<Int> by lazy {
        BroadcastChannel<Int>(Channel.CONFLATED)
    }

    @Test
    fun main() = runBlocking {
        val now = System.currentTimeMillis()
        channel.send(-1)

        launch {
            channel.asFlow().collect {
                println("consumer1: ${System.currentTimeMillis() - now} $it")
            }
        }
        launch {
            delay(1900)
            channel.asFlow().collect {
                println("consumer2: ${System.currentTimeMillis() - now} $it")
            }
        }
        launch {
//            repeat(1000) {
//                delay(500)
//                channel.send(it)
//            }
            delay(500)
            channel.send(1)
            delay(1000)
            channel.send(2)
            delay(2000)
            channel.send(3)
        }

    }.let { Unit }

    var count = 0

    @Test
    fun test() = runBlocking {
//        val dispatchers = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val dispatchers = Executors.newFixedThreadPool(100).asCoroutineDispatcher()

        coroutineScope {
            launch(dispatchers) {
                for (i in 0 until 1000000) {
                    count += 1
                }
            }
            launch(dispatchers) {
                for (i in 0 until 1000000) {
                    count += 1
                }
            }
        }
        println(count)
        Unit
    }

    @Test
    fun testzz() = runBlocking {
        val channel = Channel<Int>(2)
        val start = System.currentTimeMillis()

        withContext(Dispatchers.IO) {
            channel.consumeAsFlow()
                .onEach {
                    delay(3000)
                    println(it)

                    if (it == 1) {
                        val now = System.currentTimeMillis()
                        println(now - start)
                    }
                }.launchIn(this)

            repeat(2) {
                delay(1000)
                channel.send(it)
            }
        }
        Unit
    }

}