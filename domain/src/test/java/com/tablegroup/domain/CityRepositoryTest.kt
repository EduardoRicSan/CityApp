package com.tablegroup.domain

import app.cash.turbine.test
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.local.room.entities.CityEntity
import com.tablegroup.data.remote.api.ApiService
import com.tablegroup.data.remote.dto.CityDto
import com.tablegroup.data.remote.dto.CoordDto
import com.tablegroup.domain.repository.CityRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CityRepositoryTest {

    private val api: ApiService = mockk()
    private val dao: CityDao = mockk()
    private val dataStore: CityDataStore = mockk()
    private lateinit var repository: CityRepository

    @Before
    fun setup() {
        repository = CityRepository(api, dao, dataStore)
    }

    @Test
    fun `syncCitiesIfNeeded inserts cities when DB is empty`() = runTest {
        // Mocks
        coEvery { dao.getAllCities() } returns emptyList()
        val sampleCityDtos = listOf(
            CityDto(1, "Berlin", "DE", CoordDto(13.4, 52.52)),
            CityDto(2, "Rome", "IT", CoordDto(12.5, 41.9))
        )
        coEvery { api.getCities() } returns sampleCityDtos
        coEvery { dao.insertCities(any()) } just Runs

        // Ejecuta
        repository.syncCitiesIfNeeded()

        // Verifica que insertCities fue llamado con los datos convertidos a entity
        coVerify { dao.insertCities(match { it.size == 2 }) }
    }


    @Test
    fun `getCities should emit sorted success list`() = runTest {
        val cityEntities = listOf(
            CityEntity(2, "Berlin", "DE", 52.52, 13.405),
            CityEntity(1, "Amsterdam", "NL", 52.3676, 4.9041)
        )
        every { dao.getAllCitiesFlow() } returns flowOf(cityEntities)

        repository.getCities().test {
            val emission = awaitItem()
            assertTrue(emission is NetworkResult.Success)
            val cities = (emission as NetworkResult.Success).data
            assertEquals(2, cities.size)
            // Verificar orden alfabético por nombre (amsterdam antes de berlin)
            assertEquals("Amsterdam", cities[0].name)
            assertEquals("Berlin", cities[1].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite calls DataStore correctly`() = runTest {
        coEvery { dataStore.toggleFavorite(1) } just Runs

        repository.toggleFavorite(1)

        coVerify { dataStore.toggleFavorite(1) }
    }

    @Test
    fun `getCityById returns correct domain model`() = runTest {
        val entity = CityEntity(1, "Lima", "PE", -12.0, -77.0)

        coEvery { dao.getCityById(1) } returns entity

        val result = repository.getCityById(1)

        assertNotNull(result)
        assertEquals("Lima", result?.name)
    }
}

