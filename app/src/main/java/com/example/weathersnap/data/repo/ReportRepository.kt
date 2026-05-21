package com.example.weathersnap.data.repo

import com.example.weathersnap.data.offline.ReportDao
import com.example.weathersnap.data.offline.ReportEntity
import com.example.weathersnap.screens.saved_reports.SavedReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val dao: ReportDao
) {

    suspend fun insertReport(report: SavedReport) {
        withContext(Dispatchers.IO) {
            val entity = ReportEntity(
                cityName = report.cityName,
                temperature = report.temperature,
                condition = report.condition,
                humidity = report.humidity,
                windSpeed = report.windSpeed,
                pressure = report.pressure,
                notes = report.notes,
                imagePath = report.imagePath,
                originalSizeMb = report.originalSizeMb,
                compressedSizeMb = report.compressedSizeMb,
                timestamp = report.timestamp
            )
            dao.insertReport(entity)
        }
    }

    fun getAllReports(): Flow<List<SavedReport>> {
        return dao.getAllReports().map { entities ->
            entities.map { entity ->
                SavedReport(
                    id = entity.id,
                    cityName = entity.cityName,
                    temperature = entity.temperature,
                    condition = entity.condition,
                    humidity = entity.humidity,
                    windSpeed = entity.windSpeed,
                    pressure = entity.pressure,
                    notes = entity.notes,
                    imagePath = entity.imagePath,
                    originalSizeMb = entity.originalSizeMb,
                    compressedSizeMb = entity.compressedSizeMb,
                    timestamp = entity.timestamp
                )
            }
        }
    }
}