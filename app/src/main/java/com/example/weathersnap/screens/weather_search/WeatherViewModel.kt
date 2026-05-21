package com.example.weathersnap.screen.weather_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.online.CityDto
import com.example.weathersnap.data.repo.WeatherRepository
import com.example.weathersnap.screens.weather_search.WeatherData
import com.example.weathersnap.screens.weather_search.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _citySuggestions = MutableStateFlow<List<CityDto>>(emptyList())
    val citySuggestions = _citySuggestions.asStateFlow()

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val weatherState = _weatherState.asStateFlow()

    private var lastSelectedCity = ""

    init {
        setupSearchDebounce()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val trimmedQuery = query.trim()


                    if (trimmedQuery == lastSelectedCity) {
                        _citySuggestions.value = emptyList()
                        return@collectLatest
                    }

                    if (trimmedQuery.length > 2) {
                        fetchCitySuggestions(trimmedQuery)
                    } else {
                        _citySuggestions.value = emptyList()
                    }
                }
        }
    }

    fun onManualSearch() {
        val currentQuery = _searchQuery.value.trim()

        if (currentQuery.length > 2) {
            _weatherState.value = WeatherUiState.Loading
            _citySuggestions.value = emptyList()

            viewModelScope.launch {
                val result = repository.searchCities(currentQuery)

                result.onSuccess { cities ->
                    if (cities.isNotEmpty()) {
                        // Automatically select the best match (the first result)
                        onCitySelected(cities.first())
                    } else {
                        _weatherState.value = WeatherUiState.Error("No city found for '$currentQuery'.")
                    }
                }
                result.onFailure {
                    _weatherState.value = WeatherUiState.Error("Failed to search. Please check your connection.")
                }
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            _citySuggestions.value = emptyList()            // Hide dropdown
            _weatherState.value = WeatherUiState.Idle       // Reset weather card to default
            lastSelectedCity = ""                           // Clear the selection memory
        }
    }

    fun clearSuggestions() {
        _citySuggestions.value = emptyList()
    }

    private suspend fun fetchCitySuggestions(query: String) {
        val result = repository.searchCities(query)
        result.onSuccess { cities ->
            _citySuggestions.value = cities
        }
    }

    fun onCitySelected(city: CityDto) {
        val formattedName = "${city.name}, ${city.country}"
        lastSelectedCity = formattedName

        _searchQuery.value = formattedName
        _citySuggestions.value = emptyList() // Hide dropdown

        fetchWeather(city)
    }

    private fun fetchWeather(city: CityDto) {
        _weatherState.value = WeatherUiState.Loading

        viewModelScope.launch {
            val result = repository.getWeather(city.latitude, city.longitude)
            result.onSuccess { response ->
                val current = response.current
                val weatherData = WeatherData(
                    cityName = "${city.name}, ${city.country}",
                    temperature = current.temperature.toString(),
                    condition = mapWeatherCodeToCondition(current.weatherCode),
                    humidity = current.humidity.toString(),
                    windSpeed = current.windSpeed.toString(),
                    pressure = current.pressure.toString()
                )
                _weatherState.value = WeatherUiState.Success(weatherData)
            }
            result.onFailure { exception ->
                _weatherState.value = WeatherUiState.Error("An error occurred fetching weather.")
            }
        }
    }

    private fun mapWeatherCodeToCondition(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Partly cloudy"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            56, 57 -> "Freezing drizzle"
            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing rain"
            71, 73, 75 -> "Snow"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95 -> "Thunderstorm"
            96, 99 -> "Hail storm"
            else -> "Unknown"
        }
    }
}