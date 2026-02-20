package com.example.week5.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week5.viewmodel.WeatherViewModel
import java.util.concurrent.TimeUnit

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val uiState by weatherViewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.cityInput,
                onValueChange = { weatherViewModel.updateCityInput(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("City") },
                singleLine = true
            )

            Button(
                onClick = { weatherViewModel.fetchWeather() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get weather data")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.errorMessage?.let { msg ->
                Text(text = msg)
            }

            val weather = uiState.cachedWeather
            if (weather != null) {
                Text(text = "City: ${weather.city}")
                Text(text = "Temperature: ${weather.temp} °C")
                Text(text = "Description: ${weather.description}")

                val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(
                    System.currentTimeMillis() - weather.fetchedAt
                )
                Text(text = "Saved: $minutesAgo min ago")
            } else {
                Text(text = "No saved weather yet. Search a city to save it.")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}