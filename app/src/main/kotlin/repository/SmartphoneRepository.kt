package repository

import com.mongodb.client.MongoCollection
import model.Smartphone
import org.bson.Document
import org.bson.types.ObjectId
import utils.DatabaseConnector

class SmartphoneRepository {
    private val collectionName = "data"
    private val collection: MongoCollection<Document> =
        DatabaseConnector.database.getCollection(collectionName)

    // Get all smartphones
    fun getAllSmartphones(): List<Smartphone> {
        return collection.find().map { document ->
            mapToSmartphone(document)
        }.toList()
    }

    // Get smartphone by ID
    fun getSmartphoneById(objectId: String): Smartphone? {
        val mongoObjectId = try {
            ObjectId(objectId)
        } catch (e: IllegalArgumentException) {
            return null // Return null if the objectId is invalid
        }
        val document = collection.find(Document("_id", mongoObjectId)).first() ?: return null
        return mapToSmartphone(document)
    }

    // Search smartphones with filters
    fun searchSmartphones(
        brand: String?,
        memory: String?,
        camera: String?,
        minBudget: Double?,
        maxBudget: Double?
    ): List<Smartphone> {
        val query = buildQuery(brand, memory, camera, minBudget, maxBudget)
        return collection.find(query).map { document ->
            mapToSmartphone(document)
        }.toList()
    }

    // Helper to build dynamic queries
    private fun buildQuery(
        brand: String?,
        memory: String?,
        camera: String?,
        minBudget: Double?,
        maxBudget: Double?
    ): Document {
        val query = Document()

        brand?.let { query["Brand"] = it }
        memory?.let { query["Storage and RAM"] = it }
        camera?.let { query["Camera"] = Document("\$regex", camera).append("\$options", "i") }
        minBudget?.let { query["Price"] = Document("\$gte", it) }
        maxBudget?.let {
            query["Price"] = query["Price"]?.let { price ->
                (price as Document).append("\$lte", it)
            } ?: Document("\$lte", it)
        }

        return query
    }

    // Map MongoDB document to Smartphone object
    private fun mapToSmartphone(document: Document): Smartphone {
        return Smartphone(
            _id = document.getObjectId("_id").toHexString(), // Convert ObjectId to String
            brand = document.getString("Brand"),
            model = document.getString("Phone Name"),
            processor = document.getString("Processor"),
            storageAndRam = document.getString("Storage and RAM"),
            dimensions = document.getString("Dimensions"),
            display = document.getString("Display"),
            camera = document.getString("Camera"),
            batteryAndCharging = document.getString("Battery & Charging"),
            sensors = document.getString("Sensors"),
            operatingSystem = document.getString("Operating System"),
            price = when (val price = document["Price"]) {
                is Int -> price.toDouble() // Convert Int to Double
                is Double -> price // Use as-is if already a Double
                else -> 0.0 // Fallback for unexpected types
            },
            image = document.getString("Image")
        )
    }
}
