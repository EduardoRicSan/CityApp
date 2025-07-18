package com.tablegroup.data.remote.api.cities

import android.util.Log
import com.tablegroup.data.remote.dto.CityDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * Implementation of [ApiService] using Ktor HttpClient to fetch city data.
 */
class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    /**
     * Fetches cities as JSON text and decodes into a list of CityDto.
     */
    override suspend fun getCities(): List<CityDto> {
        val responseText = client.get(ApiConstants.GET_CITIES).bodyAsText()
        Log.d("ApiServiceImpl", "Raw response length: ${responseText.length}")
        val cityList = JsonUtils.json.decodeFromString<List<CityDto>>(responseText)
        Log.d("ApiServiceImpl", "Parsed cities count: ${cityList.size}")
        return cityList
    }
}

/**
 * Singleton object holding a configured Json instance for deserialization.
 */
object JsonUtils {
    val json = Json {
        ignoreUnknownKeys = true // Ignore unknown JSON keys to avoid errors on extra fields
        isLenient = true         // Allow lenient parsing for flexible JSON formats
    }
}
