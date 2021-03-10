package dylan.kwon.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class `13_StateFlow` {


    /**
     * LiveData와 비슷.
     */
    private val stateFlow = MutableStateFlow(-1)

    /**
     * replay 개수 만큼의 이전 값을을 replayCache에 저장한다.
     */
    private val sharedFlow = MutableSharedFlow<Int>(5)

    @Test
    fun testByState() = runBlocking {
        println("init: ${stateFlow.value}")

        launch {
            stateFlow.collect {
                println("collect1: $it")
            }
        }
        launch {
            stateFlow.collect {
                println("collect2: $it")
            }
        }
        launch {
            for (i in 0 until 10) {
                delay(100)
                stateFlow.value = i
            }
        }.join()

        println("final: ${stateFlow.value}")
    }

    @Test
    fun testByShared() = runBlocking {
        launch {
            sharedFlow.collect {
                println("collect1: $it")
            }
        }
        launch {
            sharedFlow.collect {
                println("collect2: $it")
            }
        }
        launch {
            for (i in 0 until 10) {
                delay(100)
                sharedFlow.emit(i)
            }
        }.join()

        println("final: ${sharedFlow.replayCache}")
    }

}