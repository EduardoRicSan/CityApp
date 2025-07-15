package com.tablegroup.data

import com.tablegroup.data.remote.api.ApiServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ApiServiceImplTest {

    private lateinit var apiService: ApiServiceImpl

    @Before
    fun setUp() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json" -> {
                            respond(
                                content = """
                                    [
                                        {
                                            "_id": 1,
                                            "name": "Barcelona",
                                            "country": "ES",
                                            "coord": {
                                                "lat": 41.3851,
                                                "lon": 2.1734
                                            }
                                        },
                                        {
                                            "_id": 2,
                                            "name": "Berlin",
                                            "country": "DE",
                                            "coord": {
                                                "lat": 52.52,
                                                "lon": 13.405
                                            }
                                        }
                                    ]
                                """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers = headersOf("Content-Type" to listOf("application/json"))
                            )
                        }

                        else -> respond(
                            content = "Not Found",
                            status = HttpStatusCode.NotFound,
                            headers = headersOf("Content-Type" to listOf("text/plain"))
                        )
                    }
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        apiService = ApiServiceImpl(client)
    }

    @Test
    fun `getCities returns list of CityDto`() = runTest {
        val cities = apiService.getCities()

        assertEquals(2, cities.size)
        assertEquals("Barcelona", cities[0].name)
        assertEquals("ES", cities[0].country)
        assertEquals(41.3851, cities[0].coord.lat, 0.0001)
    }

    @Test(expected = SerializationException::class)
    fun `getCities throws exception on invalid json`() = runTest {
        val badClient = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                        content = "invalid json",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type" to listOf("application/json"))
                    )
                }
            }
        }

        val badApiService = ApiServiceImpl(badClient)
        badApiService.getCities()
    }
}

