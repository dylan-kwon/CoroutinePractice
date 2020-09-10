package dylan.kwon.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("ClassName")
class `12_CallbackFlow` {

    private val view by lazy {
        View()
    }

    @Test
    fun main() = runBlocking {
        val clickFlow = launch {
            callbackFlow {
                var clickCount = 0

                view.setOnClickListener {
                    if (!isClosedForSend) offer(++clickCount)
                }
                awaitClose()
            }.catch {
                println("error: ${it.message}")

            }.onCompletion {
                println("completed.")
            }.collectLatest {
                println("clickCount: $it")
            }
        }
        val startAutoClick = launch {
            clickView()
        }
        launch {
            delay(3000)
            clickFlow.cancelAndJoin()
            startAutoClick.cancelAndJoin()
        }
    }.let { Unit }

    private suspend fun clickView() {
        while (true) {
            delay(200)
            view.click()
        }
    }

    class View {
        private var onClickListener: (() -> Unit)? = null

        fun setOnClickListener(listener: (() -> Unit)?) {
            this.onClickListener = listener
        }

        fun click() = onClickListener?.let { it() }
    }

    val flow = flow {
        while (true) {
            delay(1000)
            emit(Thread.currentThread().name)
        }
    }

    @Test
    fun test() = runBlocking {
        withContext(Dispatchers.IO) {
            flow.collect { println(it) }
        }
    }

}