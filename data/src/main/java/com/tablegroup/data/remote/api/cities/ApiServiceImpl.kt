package com.tablegroup.data.remote.api.cities

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
        return JsonUtils.json.decodeFromString(responseText)
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
