package com.example.week5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week5.BuildConfig
import com.example.week5.data.model.WeatherResponse
import com.example.week5.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val cityInput: String = "",
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val errorMessage: String? = null
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

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
            _uiState.update { it.copy(isLoading = true, errorMessage = null, weather = null) }

            try {
                val result = RetrofitInstance.api.getWeatherByCity(
                    city = city,
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )

                _uiState.update { it.copy(isLoading = false, weather = result) }
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