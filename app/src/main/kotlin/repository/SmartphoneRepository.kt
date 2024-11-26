package repository

import com.mongodb.client.MongoCollection
import model.Smartphone
import org.bson.Document
import utils.DatabaseConnector
import org.bson.types.ObjectId // For working with MongoDB ObjectId

class SmartphoneRepository {

    private val collection: MongoCollection<Document> =
        DatabaseConnector.database.getCollection("data")

    fun getAllSmartphones(): List<Smartphone> {
        return collection.find().map { document -> mapToSmartphone(document) }.toList()
    }

    fun searchSmartphones(query: String?): List<Smartphone> {
        val filter = if (query.isNullOrBlank()) Document() else Document("brand", query)
        return collection.find(filter).map { document -> mapToSmartphone(document) }.toList()
    }

    fun getSmartphoneById(id: String): Smartphone? {
        val filter = Document("_id", ObjectId(id))
        val document = collection.find(filter).firstOrNull()
        return document?.let { mapToSmartphone(it) }
    }


    fun getRecommendations(
        brand: String? = null,
        priceRange: String? = null,
        camera: String? = null,
        storage: String? = null,
        operatingSystem: String? = null,
        display: String? = null,
        battery: String? = null,
        processor: String? = null
    ): List<Smartphone> {
        val filter = Document()

        // Debug received parameters
        println("Parameters: brand=$brand, priceRange=$priceRange, camera=$camera, storage=$storage, operatingSystem=$operatingSystem, display=$display, battery=$battery, processor=$processor")

        // Add filters dynamically
        brand?.let {
            filter["Brand"] = Document("\$regex", it).append("\$options", "i") // Case-insensitive
        }
        priceRange?.let {
            val range = it.split("-")
            if (range.size == 2) {
                val minPrice = range[0].toDoubleOrNull()
                val maxPrice = range[1].toDoubleOrNull()
                if (minPrice != null && maxPrice != null) {
                    filter["Price"] = Document("\$gte", minPrice).append("\$lte", maxPrice)
                }
            }
        }
        camera?.let {
            filter["Camera"] = Document("\$regex", it).append("\$options", "i")
        }
        storage?.let {
            filter["Storage and RAM"] = Document("\$regex", it).append("\$options", "i")
        }
        operatingSystem?.let {
            filter["Operating System"] = Document("\$regex", it).append("\$options", "i")
        }
        display?.let {
            filter["Display"] = Document("\$regex", it).append("\$options", "i")
        }
        battery?.let {
            filter["Battery & Charging"] = Document("\$regex", it).append("\$options", "i")
        }
        processor?.let {
            filter["Processor"] = Document("\$regex", it).append("\$options", "i")
        }

        // Debug constructed filter
        println("Constructed filter: $filter")

        return try {
            collection.find(filter).limit(6).map { document -> mapToSmartphone(document) }.toList()
        } catch (e: Exception) {
            println("Error executing query: ${e.message}")
            emptyList() // Return an empty list in case of an error
        }
    }






    private fun mapToSmartphone(document: Document): Smartphone {
        return Smartphone(
            _id = document.getObjectId("_id").toHexString(),
            brand = document.getString("Brand") ?: "Unknown Brand",
            model = document.getString("Phone Name") ?: "Unknown Model",
            processor = document.getString("Processor") ?: "Unknown Processor",
            storageAndRam = document.getString("Storage and RAM") ?: "Unknown Storage",
            dimensions = document.getString("Dimensions") ?: "Unknown Dimensions",
            display = document.getString("Display") ?: "Unknown Display",
            camera = document.getString("Camera") ?: "Unknown Camera",
            batteryAndCharging = document.getString("Battery & Charging") ?: "Unknown Battery",
            sensors = document.getString("Sensors") ?: "Unknown Sensors",
            operatingSystem = document.getString("Operating System") ?: "Unknown OS",
            price = document.getDouble("Price") ?: 0.0,
            image = document.getString("Image") ?: "https://example.com/default-image.jpg"
        )
    }
}
