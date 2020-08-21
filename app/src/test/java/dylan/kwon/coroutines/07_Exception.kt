package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Unconfined: 기본적으로 부모의 스레드를 사용하지만, 중단함수를 만난 이후 다시 재개한 스레드를 사용.
 */
@Suppress("ClassName")
class `07_Exception` {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("exception: $exception")
    }

    @Test
    fun main() {
        runBlocking {
            launch()
            async()
            exceptCancel()
        }
        println("end.")
        println()
    }

    private suspend fun launch() {
        GlobalScope.launch(exceptionHandler) {
            println("start launch.")
            throw NullPointerException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
        }.join()
        println("end launch")
        println()
    }

    private suspend fun async() {
        val deferred = GlobalScope.async(exceptionHandler) {
            println("start async.")
            throw ArithmeticException() // Nothing is printed, relying on user to call await
        }
        try {
            deferred.await()
            println("end async.")

        } catch (e: ArithmeticException) {
            println("exception async.")
        }
        println()
    }

    private suspend fun exceptCancel() {
        coroutineScope {
            try {
                launch {
                    try {
                        println("Child is sleeping")
                        delay(Long.MAX_VALUE)
                    } finally {
                        println("Child is cancelled")
                    }
                }
                launch {
                    println("Throwing exception from scope")
                    throw AssertionError()
                }

            } catch (e: AssertionError) {
                println("Caught assertion error")
            }
        }
        println("exceptCancel end.")
        println()
    }
}