package server

import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.core.RSocketServer
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.tcp.TcpServer
import io.rsocket.kotlin.transport.ktor.tcp.TcpServerTransport
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Character
import models.Spell
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private const val PORT = 8080
private const val HOST_NAME = "localhost"
private val json = Json { ignoreUnknownKeys = true }
private const val MAX_SPELLS = 50

class Server(val character: Character) {

    // Se crea el servidor
    private val transport = TcpServerTransport(HOST_NAME, PORT)
    private val connector = RSocketServer {
        // conguración del servidor, si no podemos usar RSocketServer() por defecto
        maxFragmentSize = 1024
    }

    init {
        println("\uD83E\uDDD9 ${character.name} esta escuchado en: $PORT")
    }


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun start() {
        val server: TcpServer = connector.bind(transport) {
            // Handler para peticiones y respuestas
            peticionesRespuestas()
        }
        server.handlerJob.join() // hacemos el join!
    }

    // Handler para peticiones y respuestas
    private fun peticionesRespuestas() = RSocketRequestHandler {
        //handler for request/response
        requestResponse { request: Payload ->
            // request payload
            println("\uD83D\uDCAC ${character.name} -> Ha recibido: ${request.data.readText()}")
            // return response payload
            buildPayload {
                data("\uD83D\uDE4B Hola soy el cofre de ${character.name}")
            }
        }

        //handler for request/stream con flow
        requestStream { request: Payload ->
            //println("Server -> ${request.data.readText()}") //print request payload data
            flow {
                repeat(MAX_SPELLS) { i ->
                    delay((300..700).random().toLong())
                    // println("\uD83D\uDCAC ${character.name} -> Ha metido un nuevo hechizo ${i + 1} en \uD83C\uDF81")
                    emit(
                        buildPayload {
                            data(
                                json.encodeToString<Spell>(
                                    Spell(
                                        id = (i + 1),
                                        name = "Hechizo ${i + 1}",
                                        type = Spell.Type.values().random()
                                    )
                                )
                            )
                        }
                    )
                }
            }
        }

    }


}