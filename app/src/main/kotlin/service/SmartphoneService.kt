package service

import model.Smartphone
import repository.SmartphoneRepository
import utils.FrequencyCounter
import utils.Trie

class SmartphoneService(
    private val repository: SmartphoneRepository,
    private val trie: Trie
) {
    /**
     * Fetch all smartphones.
     */
    fun getAllSmartphones(): List<Smartphone> = repository.getAllSmartphones()

    /**
     * Search smartphones by query and update search frequency.
     */
    fun searchSmartphones(query: String?): List<Smartphone> {
        query?.let { FrequencyCounter.incrementWord(it) }
        return repository.searchSmartphones(query)
    }

    /**
     * Get all keywords from brands and models, and populate the Trie.
     */
    fun getAllKeywords(): List<String> {
        val smartphones = repository.getAllSmartphones()
        val keywords = smartphones.flatMap { listOf(it.brand, it.model) }.distinct()
        keywords.forEach { trie.insert(it) }
        return keywords
    }

    fun getSmartphoneById(id: String): Smartphone? {
        return repository.getSmartphoneById(id)
    }

    /**
     * Get recommendations based on filters.
     *
     * @param brand The brand to filter by (e.g., "Apple").
     * @param priceRange The price range in "min-max" format (e.g., "1000-2000").
     * @param camera The camera description to filter by (e.g., "48MP").
     * @param storage The storage capacity to filter by (e.g., "256GB").
     * @return A filtered list of smartphones.
     */
    fun getRecommendations(
        brand: String? = null,
        priceRange: String? = null,
        camera: String? = null,
        storage: String? = null,
        operatingSystem :String? =null,
        display : String? =null,
        battery : String? =null,
        processor : String? =null,
    ): List<Smartphone> {
        return repository.getRecommendations(
            brand = brand,
            priceRange = priceRange,
            camera = camera,
            storage = storage,
            operatingSystem = operatingSystem,
            display = display,
            battery = battery,
            processor = processor
        )
    }
}
