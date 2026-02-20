package com.example.week5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.week5.BuildConfig
import com.example.week5.data.local.AppDatabase
import com.example.week5.data.model.WeatherEntity
import com.example.week5.data.remote.RetrofitInstance
import com.example.week5.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val cityInput: String = "",
    val isLoading: Boolean = false,
    val cachedWeather: WeatherEntity? = null,
    val errorMessage: String? = null
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = WeatherRepository(db.weatherDao())

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val cacheMaxAgeMillis = 30L * 60L * 1000L

    init {
        viewModelScope.launch {
            repository.latestWeather().collectLatest { latest ->
                _uiState.update { it.copy(cachedWeather = latest) }
            }
        }
    }

    fun updateCityInput(newValue: String) {
        _uiState.update { it.copy(cityInput = newValue) }
    }

    fun fetchWeather() {
        val city = uiState.value.cityInput.trim()
        if (city.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Enter a city name") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val latest = uiState.value.cachedWeather
            val now = System.currentTimeMillis()

            val canUseCache =
                latest != null &&
                        latest.city.equals(city, ignoreCase = true) &&
                        (now - latest.fetchedAt) <= cacheMaxAgeMillis

            if (canUseCache) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            try {
                val result = RetrofitInstance.api.getWeatherByCity(
                    city = city,
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )

                val entity = WeatherEntity(
                    city = result.name,
                    temp = result.main.temp,
                    description = result.weather.firstOrNull()?.description ?: "-",
                    fetchedAt = now
                )

                repository.saveWeather(entity)

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Weather data retrieval failed. Check the name of the city and internet connection."
                    )
                }
            }
        }
    }
}