package com.example.weathersnap.data.repo

import com.example.weathersnap.data.offline.ReportDao
import com.example.weathersnap.data.offline.ReportEntity
import com.example.weathersnap.data.online.OpenMeteoApi
import com.example.weathersnap.data.online.CityDto
import com.example.weathersnap.data.online.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: OpenMeteoApi,
    private val dao: ReportDao
) {


    private val searchCache = mutableMapOf<String, List<CityDto>>()

    suspend fun searchCities(query: String): Result<List<CityDto>> {
        return withContext(Dispatchers.IO) {

            searchCache[query]?.let {
                return@withContext Result.success(it)
            }

            try {
                val response = api.searchCities(query = query)
                if (response.isSuccessful) {
                    val cities = response.body()?.results ?: emptyList()
                    searchCache[query] = cities // Save to cache
                    Result.success(cities)
                } else {
                    Result.failure(Exception("Failed to fetch cities: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getWeather(lat = lat, lon = lon)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch weather: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun saveReport(report: ReportEntity) {
        withContext(Dispatchers.IO) {
            dao.insertReport(report)
        }
    }

    fun getSavedReports(): Flow<List<ReportEntity>> {
        return dao.getAllReports()
    }
}