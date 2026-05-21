package com.example.weathersnap.data.online

import com.google.gson.annotations.SerializedName


data class GeocodingResponse(
    val results: List<CityDto>? = null
)

data class CityDto(
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)


data class WeatherResponse(
    val current: CurrentWeatherDto
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("surface_pressure") val pressure: Double,
    @SerializedName("weather_code") val weatherCode: Int
)