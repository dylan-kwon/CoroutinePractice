package dylan.kwon.coroutines

import kotlinx.coroutines.*
import org.junit.Test

@Suppress("ClassName")
class `07_Exception` {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("exception: $exception")
    }

    @Test
    fun launch() = runBlocking {
        GlobalScope.launch(exceptionHandler) {
            println("start launch.")
            throw NullPointerException()
        }.join()
        println("end launch")
        println()
    }

    @Test
    fun async() = runBlocking {
        val deferred = GlobalScope.async(exceptionHandler) {
            println("start async.")
            throw ArithmeticException()
        }
        try {
            deferred.await()
            println("end async.")

        } catch (e: ArithmeticException) {
            println("exception async.")
        }
        println()
    }

    @Test
    fun exceptCancel() = runBlocking {
        withContext(Dispatchers.IO) {
            launch {
                try {
                    println("Child is sleeping")
                    delay(5000)
                    println("Child is end")
                } finally {
                    println("Child is cancelled")
                }
            }
            launch(SupervisorJob()) {
                println("Throwing exception from scope")
                throw Exception()
            }

        }
        println("exceptCancel end.")
        println()
    }

    @Test
    fun exceptTryCatch() = runBlocking {
        println("exceptTryCatch start.")
        val handler = CoroutineExceptionHandler { _, _ ->
            println("catch1!!!!!")
        }
        try {
            withContext(Dispatchers.IO) {
                try {
                    launch {
                        try {
                            launch {
                                try {
                                    launch {
                                        try {
                                            launch {
//                                                try {
                                                throw Exception("throw except!!")
//                                                } catch (e: Exception) {
//                                                    println("catch2!!!!!")
//                                                } finally {
//                                                    println("finally6")
//                                                }
                                            }
                                        } finally {
                                            println("finally5")
                                        }
                                    }
                                } finally {
                                    println("finally4")
                                }
                            }
                        } finally {
                            println("finally3")
                        }
                    }
                } finally {
                    println("finally2")
                }
            }
        } catch (e: Exception) {
            println("catch3!!!!!")
        } finally {
            println("finally1")
        }
        println("exceptTryCatch end.")
        println()
    }
}