package com.example.weathersnap.data.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val temperature: String,
    val condition: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String,
    val imagePath: String,
    val originalSizeMb: String,
    val compressedSizeMb: String,
    val notes: String,
    val timestamp: Long = System.currentTimeMillis()
)