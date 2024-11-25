package service

import model.Smartphone
import repository.SmartphoneRepository

class SmartphoneService(private val repository: SmartphoneRepository) {

    fun getAllSmartphones(): List<Smartphone> = repository.getAllSmartphones()

    fun getSmartphoneById(objectId: String): Smartphone? = repository.getSmartphoneById(objectId)
    fun searchSmartphones(
        brand: String? = null,
        memory: String? = null,
        camera: String? = null,
        minBudget: Double? = null,
        maxBudget: Double? = null
    ): List<Smartphone> {
        return repository.getAllSmartphones().filter { smartphone ->
            (brand == null || smartphone.brand.equals(brand, ignoreCase = true)) &&
                    (memory == null || smartphone.storageAndRam.contains(memory, ignoreCase = true)) &&
                    (camera == null || smartphone.camera.contains(camera, ignoreCase = true)) &&
                    (minBudget == null || smartphone.price >= minBudget) &&
                    (maxBudget == null || smartphone.price <= maxBudget)
        }
    }
}
