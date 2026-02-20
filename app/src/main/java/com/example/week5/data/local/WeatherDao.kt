package com.example.week5.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.week5.data.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherEntity)

    @Query("SELECT * FROM weather ORDER BY fetchedAt DESC LIMIT 1")
    fun getLatestWeather(): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather ORDER BY fetchedAt DESC")
    fun getWeatherHistory(): Flow<List<WeatherEntity>>

    @Query("DELETE FROM weather")
    suspend fun clearAll()
}