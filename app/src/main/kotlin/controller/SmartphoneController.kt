package controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import service.SmartphoneService

fun Route.controller(service: SmartphoneService) {
    route("/api/smartphones") {

        // Get all smartphones or search smartphones
        get {
            try {
                val brand = call.request.queryParameters["brand"]
                val memory = call.request.queryParameters["memory"]
                val camera = call.request.queryParameters["camera"]
                val minBudget = call.request.queryParameters["minBudget"]?.toDoubleOrNull()
                val maxBudget = call.request.queryParameters["maxBudget"]?.toDoubleOrNull()

                if (minBudget != null && maxBudget != null && minBudget > maxBudget) {
                    call.respond(HttpStatusCode.BadRequest, "minBudget cannot be greater than maxBudget")
                    return@get
                }

                // Fetch smartphones with optional search filters
                val smartphones = service.searchSmartphones(
                    brand = brand,
                    memory = memory,
                    camera = camera,
                    minBudget = minBudget,
                    maxBudget = maxBudget
                )

                if (smartphones.isNotEmpty()) {
                    call.respond(smartphones)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No smartphones found matching the criteria")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        // Get smartphone by _id
        get("/{id}") {
            try {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing _id")
                    return@get
                }

                // Fetch smartphone by ID
                val smartphone = service.getSmartphoneById(id)
                if (smartphone != null) {
                    call.respond(smartphone)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Smartphone not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }
    }
}
