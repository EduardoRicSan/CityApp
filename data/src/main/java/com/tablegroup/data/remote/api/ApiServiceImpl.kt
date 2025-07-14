package com.tablegroup.data.remote.api

import android.util.Log
import com.tablegroup.data.remote.dto.CityDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getCities(): List<CityDto> {
        val responseText = client.get(ApiConstants.GET_CITIES).bodyAsText()
        return JsonUtils.json.decodeFromString(responseText)
    }

}

object JsonUtils {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}