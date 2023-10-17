import client.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.Character
import server.Server

fun main(args: Array<String>): Unit = runBlocking {
    println("Hola Reactive Sockets!")

    val serverJob = launch(Dispatchers.IO) {
        val server = Server(Character.DUMBLEDORE)
        server.start()
    }

    val clientJobsList = Character.values()
        .filter { it != Character.DUMBLEDORE }
        .map { character ->
            launch(Dispatchers.IO) {
                val client = Client(character, 3)
                client.start()
            }
        }


    // Esperamos a que terminen
    clientJobsList.joinAll()
    serverJob.join()

}
