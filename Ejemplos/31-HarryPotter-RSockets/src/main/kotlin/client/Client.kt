package client

import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.tcp.TcpClientTransport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.serialization.json.Json
import models.Character
import models.Spell
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private val json = Json { ignoreUnknownKeys = true }

private const val PORT = 8080
private const val HOST_NAME = "localhost"

class Client(val character: Character, val maxSpells: Int = 2) {

    init {
        println("\uD83E\uDDD9 ${character.name} y voy a acceder a nuestro cofre secreto \uD83C\uDF81 para obtener $maxSpells hechizos")
    }


    private val transport = TcpClientTransport(HOST_NAME, PORT)
    private val connector = RSocketConnector {

    }

    suspend fun start() {
        // conectamos al servidor
        val rsocket = connector.connect(transport).also {
            println("\uD83D\uDE4B\uD83C\uDFFB ${character.name} se ha conectado")
        }
        // hacemos una petición y esperamos la respuesta
        saludar(rsocket)
        // procesamos los flows
        peticionesStream(rsocket)

    }


    // Handler para saludar
    private suspend fun saludar(rsocket: RSocket) {
        val response = rsocket.requestResponse(
            buildPayload {
                data("\uD83D\uDC4B Hola, soy ${character.name} y voy a acceder a nuestro cofre secreto \uD83C\uDF81")
            }
        )
        // Mostramos la respuesta
        println("\uD83D\uDE4B\uD83C\uDFFB ${character.name} -> ${response.data.readText()}")
    }

    private suspend fun peticionesStream(rsocket: RSocket) {
        var actualSpell = 0
        val stream: Flow<Payload> = rsocket.requestStream(
            buildPayload {
                data("Hola desde ${character.name}")
            }
        )

        // Mostramos los maxSpell primeros primeros elementos
        stream.map { payload: Payload ->
            json.decodeFromString<Spell>(payload.data.readText())
        }.filter {
            if (character == Character.HARRY_POTTER && it.type == Spell.Type.ATTACK) {
                true
            } else if (character == Character.RON_WEASLEY && it.type == Spell.Type.DEFENSE) {
                true
            } else character == Character.HERMIONE_GRANGER && it.type == Spell.Type.HEAL
        }.take(maxSpells).collect { spell ->
            delay((200..500).random().toLong())
            println("\uD83D\uDE4B\uD83C\uDFFB ${character.name} -> Conozco el hechizo \uD83D\uDCA5 ${spell.name} y lo voy a usar por que es de tipo ${spell.type} y es el hechizo número ${++actualSpell}/$maxSpells")
        }
    }
}