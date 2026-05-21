package com.example.weathersnap.data.online

import com.example.weathersnap.data.online.GeocodingResponse
import com.example.weathersnap.data.online.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCities(
        @Query("name") query: String,
        @Query("count") count: Int = 10
    ): Response<GeocodingResponse>

    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") currentParams: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,surface_pressure,weather_code"
    ): Response<WeatherResponse>
}