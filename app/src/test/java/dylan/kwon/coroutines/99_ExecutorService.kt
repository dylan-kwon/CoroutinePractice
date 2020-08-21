package dylan.kwon.coroutines

import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("ClassName")
class `99_ExecutorService` {

    private val singleExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    private val fixedExecutor: ExecutorService by lazy {
        Executors.newFixedThreadPool(10)
    }

    private val cacheExecutor: ExecutorService by lazy {
        Executors.newCachedThreadPool()
    }

    private val workStealExecutor: ExecutorService by lazy {
        Executors.newWorkStealingPool(10)
    }

    @Test
    fun single() {
        execute(singleExecutor)
    }

    @Test
    fun fixed() {
        execute(fixedExecutor)
    }

    @Test
    fun cache() {
        execute(cacheExecutor)
    }

    @Test
    fun workSteal() {
        execute(workStealExecutor)
    }

    private fun execute(executor: Executor) {
        for (i in 0 until 100) {
            executor.execute {
                Thread.sleep(2000)
                printCurrentThread("WorkSteal($i)")
            }
            Thread.sleep(100)
        }
        Thread.sleep(Long.MAX_VALUE)
    }

    private fun printCurrentThread(tag: String) {
        println("$tag: ${Thread.currentThread().name}")
    }

}