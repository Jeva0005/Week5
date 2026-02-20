package com.example.week5.data.repository

import com.example.week5.data.local.WeatherDao
import com.example.week5.data.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val dao: WeatherDao
) {
    fun latestWeather(): Flow<WeatherEntity?> = dao.getLatestWeather()

    fun history(): Flow<List<WeatherEntity>> = dao.getWeatherHistory()

    suspend fun saveWeather(entity: WeatherEntity) {
        dao.insertWeather(entity)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}