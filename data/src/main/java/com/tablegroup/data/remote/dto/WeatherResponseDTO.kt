package com.tablegroup.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing the weather API response.
 *
 * @property location Location information.
 * @property current Current weather conditions.
 */
@Serializable
data class WeatherResponseDTO(
    @SerialName("location") var location: Location? = Location(),
    @SerialName("current") var current: Current? = Current()
)

/**
 * DTO for location details.
 *
 * @property name City name.
 * @property region Region name.
 * @property country Country name.
 * @property lat Latitude coordinate.
 * @property lon Longitude coordinate.
 * @property tzId Timezone ID.
 * @property localtimeEpoch Local time in epoch seconds.
 * @property localtime Local time as string.
 */
@Serializable
data class Location(
    @SerialName("name") var name: String? = null,
    @SerialName("region") var region: String? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("lat") var lat: Double? = null,
    @SerialName("lon") var lon: Double? = null,
    @SerialName("tz_id") var tzId: String? = null,
    @SerialName("localtime_epoch") var localtimeEpoch: Int? = null,
    @SerialName("localtime") var localtime: String? = null
)

/**
 * DTO for weather condition description.
 *
 * @property text Description text of the condition.
 * @property icon Icon URL for the condition.
 * @property code Condition code.
 */
@Serializable
data class Condition(
    @SerialName("text") var text: String? = null,
    @SerialName("icon") var icon: String? = null,
    @SerialName("code") var code: Int? = null
)

/**
 * DTO for current weather data.
 *
 * @property lastUpdatedEpoch Last updated time in epoch seconds.
 * @property lastUpdated Last updated time as string.
 * @property tempC Temperature in Celsius.
 * @property tempF Temperature in Fahrenheit.
 * @property isDay Whether it is day (1) or night (0).
 * @property condition Weather condition.
 * @property windMph Wind speed in miles per hour.
 * @property windKph Wind speed in kilometers per hour.
 * @property windDegree Wind direction in degrees.
 * @property windDir Wind direction as string.
 * @property pressureMb Pressure in millibars.
 * @property pressureIn Pressure in inches.
 * @property precipMm Precipitation in millimeters.
 * @property precipIn Precipitation in inches.
 * @property humidity Humidity percentage.
 * @property cloud Cloud cover percentage.
 * @property feelslikeC Feels like temperature in Celsius.
 * @property feelslikeF Feels like temperature in Fahrenheit.
 * @property windchillC Wind chill in Celsius.
 * @property windchillF Wind chill in Fahrenheit.
 * @property heatindexC Heat index in Celsius.
 * @property heatindexF Heat index in Fahrenheit.
 * @property dewpointC Dew point in Celsius.
 * @property dewpointF Dew point in Fahrenheit.
 * @property visKm Visibility in kilometers.
 * @property visMiles Visibility in miles.
 * @property uv UV index.
 * @property gustMph Gust speed in miles per hour.
 * @property gustKph Gust speed in kilometers per hour.
 */
@Serializable
data class Current(
    @SerialName("last_updated_epoch") var lastUpdatedEpoch: Int? = null,
    @SerialName("last_updated") var lastUpdated: String? = null,
    @SerialName("temp_c") var tempC: Double? = null,
    @SerialName("temp_f") var tempF: Double? = null,
    @SerialName("is_day") var isDay: Double? = null,
    @SerialName("condition") var condition: Condition? = Condition(),
    @SerialName("wind_mph") var windMph: Double? = null,
    @SerialName("wind_kph") var windKph: Double? = null,
    @SerialName("wind_degree") var windDegree: Double? = null,
    @SerialName("wind_dir") var windDir: String? = null,
    @SerialName("pressure_mb") var pressureMb: Double? = null,
    @SerialName("pressure_in") var pressureIn: Double? = null,
    @SerialName("precip_mm") var precipMm: Double? = null,
    @SerialName("precip_in") var precipIn: Double? = null,
    @SerialName("humidity") var humidity: Double? = null,
    @SerialName("cloud") var cloud: Double? = null,
    @SerialName("feelslike_c") var feelslikeC: Double? = null,
    @SerialName("feelslike_f") var feelslikeF: Double? = null,
    @SerialName("windchill_c") var windchillC: Double? = null,
    @SerialName("windchill_f") var windchillF: Double? = null,
    @SerialName("heatindex_c") var heatindexC: Double? = null,
    @SerialName("heatindex_f") var heatindexF: Double? = null,
    @SerialName("dewpoint_c") var dewpointC: Double? = null,
    @SerialName("dewpoint_f") var dewpointF: Double? = null,
    @SerialName("vis_km") var visKm: Double? = null,
    @SerialName("vis_miles") var visMiles: Double? = null,
    @SerialName("uv") var uv: Double? = null,
    @SerialName("gust_mph") var gustMph: Double? = null,
    @SerialName("gust_kph") var gustKph: Double? = null
)
