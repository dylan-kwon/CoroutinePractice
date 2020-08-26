package dylan.kwon.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

@FlowPreview
@ExperimentalCoroutinesApi
@Suppress("ClassName")
class `11_Flow` {

    @Test
    fun collect() = runBlocking {
        flow {
            repeat(3) {
                delay(100)
                println("emit: $it")
                emit(it)
            }
        }.map {
            it * 2
        }.filter {
            it > 3
        }.collect {
            println("collect: $it")
        }
        println("all collected.")
    }

    @Test
    fun onEach() = runBlocking {
        flow {
            repeat(3) {
                println("emit: $it")
                emit(it)
            }
        }.onEach {
            println("onEach: $it")
            delay(500)
        }.collect()
    }

    @Test
    fun cancel() = runBlocking {
        launch {
            flow {
                repeat(3) {
                    delay(100)
                    println("emit: $it")
                    emit(it)
                }
            }.collect {
                println("collect: $it")
            }
        }.let {
            delay(300)
            it.cancelAndJoin()
        }
    }

    @Test
    fun flowOf() = runBlocking {
        flowOf(1, 2, 3, 4, 5).collect {
            println("flowOf: $it")
        }
    }

    @Test
    fun asFlow() = runBlocking {
        listOf(1, 2, 3, 4, 5).asFlow()
            .collect {
                println("asFlow: $it")
            }
    }

    @Test
    fun flowOn() = runBlocking {
        println("starting: [${Thread.currentThread().name}]")

        flow {
            repeat(3) {
                println("emit: [${Thread.currentThread().name}] $it")
                emit(it)
            }
        }.flowOn(
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        ).map {
            println("map: [${Thread.currentThread().name}] $it")
            it * it
        }.flowOn(
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        ).collect {
            println("collect: [${Thread.currentThread().name}] $it")
        }
    }

    @Test
    fun buffer() = runBlocking {
        measureTimeMillis {
            flow {
                repeat(3) {
                    delay(100)
                    println("emit: $it")
                    emit(it)
                }
            }.buffer()
                .collect {
                    delay(300)
                    println("collect: $it")
                }
        }.let {
            println(it)
        }
    }

    @Test
    fun conflate() = runBlocking {
        measureTimeMillis {
            flow {
                repeat(3) {
                    delay(100)
                    println("emit: $it")
                    emit(it)
                }
            }.conflate()
                .collect {
                    delay(300)
                    println("collect: $it")
                }
        }.let {
            println(it)
        }
    }

    @Test
    fun collectLatest() = runBlocking {
        flow {
            repeat(3) {
                delay(100)
                println("emit: $it")
                emit(it)
            }
        }.collectLatest() {
            try {
                delay(300)
                println("collect: $it")
            } catch (e: CancellationException) {
                println(e.message)
            }
        }
    }

    @Test
    fun zip() = runBlocking {
        val flow1 = flow {
            repeat(5) {
                delay(1000)
                println("flow1.emit: $it")
                emit(it)
            }
        }
        val flow2 = flow {
            repeat(10) {
                delay(2000)
                println("flow2.emit: $it")
                emit(it)
            }
        }
        val flow3 = flow {
            repeat(20) {
                delay(4000)
                println("flow3.emit: $it")
                emit(it)
            }
        }
        flow1.zip(flow2) { f1: Int, f2: Int ->
            "$f1, $f2"
        }.zip(flow3) { z1: String, f3: Int ->
            "$z1, $f3".also { println("\nzip: $it\n") }
        }.collect {
            println("collect: $it")
        }
    }

    @Test
    fun combine() = runBlocking {
        val flow1 = flow {
            repeat(5) {
                delay(1000)
                println("flow1.emit: $it")
                emit(it)
            }
        }
        val flow2 = flow {
            repeat(10) {
                delay(2000)
                println("flow2.emit: $it")
                emit(it)
            }
        }
        val flow3 = flow {
            repeat(15) {
                delay(4000)
                println("flow3.emit: $it")
                emit(it)
            }
        }
        flow1.combine(flow2) { f1: Int, f2: Int ->
            "$f1, $f2"
        }.combine(flow3) { z1: String, f3: Int ->
            "$z1, $f3".also { println("\ncombine: $it\n") }
        }.collect {
            println("collect: $it")
        }
    }

    @Test
    fun flatMapConcat() = runBlocking {
        flow {
            repeat(5) {
                delay(300)
                println("emit: $it")
                emit(it)
            }
        }.flatMapConcat {
            flow {
                delay(500)
                emit(it)
                emit(it + 1)
            }
        }.collect {
            println("collect: $it")
        }
    }

    @Test
    fun flatMapMerge() = runBlocking {
        flow {
            repeat(5) {
                delay(300)
                println("emit: $it")
                emit(it)
            }
        }.flatMapMerge {
            flow {
                delay(500)
                emit(it)
                emit(it + 1)
            }
        }.collect {
            println("collect: $it")
        }
    }

    @Test
    fun flatMapLatest() = runBlocking {
        flow {
            repeat(5) {
                delay(300)
                println("emit: $it")
                emit(it)
            }
        }.flatMapLatest {
            flow {
                delay(500)
                emit(it)
                emit(it + 1)
            }
        }.collect {
            println("collect: $it")
        }
    }

    @Test
    fun exception() = runBlocking {
        (0..10).asFlow()
            .onEach { delay(100) }
            .onEach { if (it >= 5) throw NullPointerException("Exception!!") }
            .catch {
                println(it.message)
            }
            .collect {
                println("collect: $it")
            }
    }

    @Test
    fun completion() = runBlocking {
        (0..10).asFlow()
            .onEach { delay(100) }
            .onEach { if (it >= 5) throw NullPointerException("Exception!!") }
            .onCompletion {
                println("e: ${it?.message}")
                println("done!!")
            }.catch { println(it.message) }
            .collect { println("collect: $it") }
    }

    @Test
    fun launchIn() = runBlocking {
        (0..10).asFlow()
            .onEach { delay(200) }
            .onEach { println(it) }
            .launchIn(this)
        println("work..")
    }

}