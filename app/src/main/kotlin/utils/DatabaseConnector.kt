package utils

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

object DatabaseConnector {
    private const val CONNECTION_STRING = "mongodb+srv://shah392:qwerty123@cluster0.al3fi.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    private const val DATABASE_NAME = "smartphones"

    val client: MongoClient by lazy {
        MongoClients.create(CONNECTION_STRING)
    }

    val database: MongoDatabase by lazy {
        client.getDatabase(DATABASE_NAME)
    }
}
