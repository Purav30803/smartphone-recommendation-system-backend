package model

import kotlinx.serialization.Serializable

@Serializable
data class Smartphone(
    val _id: String,
    val brand: String,
    val model: String,
    val processor: String,
    val storageAndRam: String,
    val dimensions: String,
    val display: String,
    val camera: String,
    val batteryAndCharging: String,
    val sensors: String,
    val operatingSystem: String,
    val price: Double,
    val image: String
)
