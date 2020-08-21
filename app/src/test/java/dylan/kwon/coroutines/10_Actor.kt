package dylan.kwon.coroutines

import kotlinx.coroutines.runBlocking
import org.junit.Test

@Suppress("ClassName")
class `10_Actor` {

    sealed class Message {
        class TextMessage(message: String) : Message()
        class ImageMessage(url: String) : Message()
    }

    @Test
    fun main() {
        runBlocking {

        }
        println("end.")
    }

}