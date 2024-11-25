package app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import controller.controller // Updated to refer to the unified controller function
import io.ktor.server.netty.*
import io.ktor.server.engine.embeddedServer
import repository.SmartphoneRepository
import service.SmartphoneService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Install Content Negotiation plugin to enable JSON serialization
    install(ContentNegotiation) {
        json()
    }

    // Install CORS plugin to handle cross-origin requests
    install(CORS) {
        anyHost() // Allow requests from any origin. Replace with specific domains in production.
        allowHeader("Content-Type")
        allowHeader("Authorization")
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
    }

    // Instantiate the repository and service layers
    val repository = SmartphoneRepository()
    val service = SmartphoneService(repository)

    // Define routes and pass the service to the controller function
    routing {
        controller(service) // Updated to use the unified controller function
    }
}
