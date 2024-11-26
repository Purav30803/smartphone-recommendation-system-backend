package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.SmartphoneService
import utils.FrequencyCounter
import utils.SpellChecker
import utils.Trie

fun Route.smartphoneController(service: SmartphoneService, trie: Trie) {
    route("/api/smartphones") {

        // Word completion
        get("/autocomplete") {
            val prefix = call.request.queryParameters["prefix"]
            println("Received prefix: $prefix") // Debugging

            if (prefix.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Prefix cannot be empty")
                return@get
            }

            val suggestions = trie.searchPrefix(prefix)
            println("Suggestions for prefix '$prefix': $suggestions") // Debugging

            call.respond(suggestions)
        }


        // Spell checking
        get("/spellcheck") {
            val query = call.request.queryParameters["query"]
            if (query.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Query cannot be empty")
                return@get
            }
            val allKeywords = service.getAllKeywords()
            val correctedWord = SpellChecker.findClosestWord(query, allKeywords)
            call.respond(correctedWord)
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing _id")
                return@get
            }

            val smartphone = service.getSmartphoneById(id)
            if (smartphone != null) {
                call.respond(smartphone)
            } else {
                call.respond(HttpStatusCode.NotFound, "Smartphone not found")
            }
        }


        // Search API with frequency tracking
        get {
            val query = call.request.queryParameters["query"]
            if (query != null) {
                FrequencyCounter.incrementWord(query)
            }
            val results = service.searchSmartphones(query)
            call.respond(results)
        }

        // Get word frequency
        get("/frequency") {
            val frequencies = FrequencyCounter.getWordFrequency()
            call.respond(frequencies)
        }

        // Recommendations endpoint
        get("/recommendations") {
            val brand = call.request.queryParameters["brand"]
            val priceRange = call.request.queryParameters["priceRange"]
            val camera = call.request.queryParameters["camera"]
            val storage = call.request.queryParameters["storage"]
            val operatingSystem = call.request.queryParameters["operatingSystem"]
            val display = call.request.queryParameters["display"]
            val battery = call.request.queryParameters["battery"]
            val processor = call.request.queryParameters["processor"]

            // Debugging: Log received parameters
            println("Received query parameters: brand=$brand, priceRange=$priceRange, camera=$camera, storage=$storage, display=$display")

            // Increment frequency counter for relevant parameters
            if (!brand.isNullOrBlank()) {
                FrequencyCounter.incrementWord(brand)
                println("Incremented frequency for brand: $brand") // Debugging
            }
            if (!camera.isNullOrBlank()) {
                FrequencyCounter.incrementWord(camera)
                println("Incremented frequency for camera: $camera") // Debugging
            }
            if (!priceRange.isNullOrBlank()) {
                FrequencyCounter.incrementWord(priceRange)
                println("Incremented frequency for price range: $priceRange") // Debugging
            }

            val recommendations = service.getRecommendations(
                brand = brand,
                priceRange = priceRange,
                camera = camera,
                storage = storage,
                operatingSystem = operatingSystem,
                display = display,
                battery = battery,
                processor = processor
            )

            call.respond(recommendations)
        }

    }
}