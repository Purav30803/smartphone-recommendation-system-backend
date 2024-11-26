package utils

import com.mongodb.client.MongoCollection
import org.bson.Document
import utils.DatabaseConnector

object FrequencyCounter {
    private val collection: MongoCollection<Document> =
        DatabaseConnector.database.getCollection("search_frequencies")

    fun incrementWord(word: String) {
        if (word.isNotBlank()) {
            val existing = collection.find(Document("word", word)).first()
            if (existing != null) {
                val newFrequency = existing.getInteger("frequency") + 1
                collection.updateOne(Document("word", word), Document("\$set", Document("frequency", newFrequency)))
                println("Updated frequency: $word -> $newFrequency")
            } else {
                collection.insertOne(Document("word", word).append("frequency", 1))
                println("Added new word: $word -> 1")
            }
        }
    }

    fun getWordFrequency(): Map<String, Int> {
        return collection.find()
            .associate { it.getString("word") to it.getInteger("frequency") }
            .also { println("Fetched word frequencies: $it") }
    }
}
