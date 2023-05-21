package com.example.edistynytandroid.ui.cityWeather

data class CityWeather(
    val name: String?,
    val main: WeatherData?,
    val wind: WindData?
)

data class WeatherData(
    val temp: Double?,
    val humidity: Double?
)

data class WindData(
    val speed: Double?
)
