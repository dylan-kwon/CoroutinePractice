package dylan.kwon.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import org.junit.Test


@ObsoleteCoroutinesApi
@Suppress("ClassName")
class `10_Actor` {

    @Test
    fun main() = runBlocking {
        val actor = actor<Message>(Dispatchers.IO) {
            var messageCount = 0
            for (message in channel) {
                when (message) {
                    is Message.TextMessage -> messageCount++
                    is Message.GetMessageCount -> println("ImageMessage: ${message.response.complete(messageCount)}")
                }
            }
        }
        sendMessages {
            actor.send(Message.TextMessage("New Message"))
        }
        val response = CompletableDeferred<Int>()
        actor.send(Message.GetMessageCount(response))
        println("messageCount: ${response.await()}")
    }

    private suspend fun sendMessages(callback: suspend () -> Unit) = withContext(Dispatchers.IO) {
        List(100) {
            launch { repeat(1000) { callback() } }
        }
    }

}

sealed class Message {
    class TextMessage(val message: String) : Message()
    class GetMessageCount(val response: CompletableDeferred<Int>) : Message()
}