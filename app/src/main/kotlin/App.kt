package app

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import controller.smartphoneController
import repository.SmartphoneRepository
import service.SmartphoneService
import utils.Trie

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) { json() }
    install(CORS) { anyHost() }

    // Initialize repository and Trie
    val repository = SmartphoneRepository()
    val trie = Trie()

    // Populate the Trie with data from the database
    trie.populateFromDatabase(repository)

    // Initialize service
    val service = SmartphoneService(repository, trie)

    // Setup routes
    routing {
        smartphoneController(service, trie)
    }
}
