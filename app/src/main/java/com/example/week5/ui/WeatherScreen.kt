package com.example.week5.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week5.viewmodel.WeatherViewModel
import java.util.Locale

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val uiState by weatherViewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(top = 48.dp).padding(16.dp)) {

        OutlinedTextField(
            value = uiState.cityInput,
            onValueChange = { weatherViewModel.updateCityInput(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("City") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { weatherViewModel.fetchWeather() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get weather data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
        }

        uiState.errorMessage?.let { msg ->
            Text(text = msg)
            Spacer(modifier = Modifier.height(12.dp))
        }

        uiState.weather?.let { data ->
            val description = data.weather.firstOrNull()?.description ?: "-"
            Text(text = "City: ${data.name}")
            Text(text = "Temperature: ${data.main.temp} °C")
            Text(text = "Description: ${description.replaceFirstChar { c -> c.titlecase(Locale.getDefault()) }}")
        }
    }
}