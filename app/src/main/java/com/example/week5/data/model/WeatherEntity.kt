package com.example.week5.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val city: String,
    val temp: Double,
    val description: String,

    val fetchedAt: Long
)