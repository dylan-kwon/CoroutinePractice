package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.Executors

@Suppress("ClassName")
class `06_Dispatchers` {

    private val singleThreadDispatcher: ExecutorCoroutineDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    private val fixedThreadDispatcher: ExecutorCoroutineDispatcher by lazy {
        Executors.newFixedThreadPool(10).asCoroutineDispatcher()
    }

    @Test
    fun main() = runBlocking {
        launch {
            printCurrentThread("None")
        }.join()

        launch(Dispatchers.IO) {
            printCurrentThread("IO")
        }.join()

        launch(Dispatchers.Default) {
            printCurrentThread("Default")
        }.join()

        launch(Dispatchers.Unconfined) {
            printCurrentThread("Unconfined")
        }.join()

        launch(singleThreadDispatcher) {
            printCurrentThread("single")
        }.join()

        launch(fixedThreadDispatcher) {
            printCurrentThread("single")
        }.join()

        singleThreadDispatcher.close()
        fixedThreadDispatcher.close()
    }

    private fun printCurrentThread(tag: String) {
        println("$tag: ${Thread.currentThread().name}")
    }

}