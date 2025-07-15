package com.tablegroup.ualaapptest

import app.cash.turbine.test
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.domain.useCase.GetCitiesUseCase
import com.tablegroup.domain.useCase.GetCityByIdUseCase
import com.tablegroup.domain.useCase.GetFavoriteIdsUseCase
import com.tablegroup.domain.useCase.SyncCitiesUseCase
import com.tablegroup.domain.useCase.ToggleFavoriteUseCase
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelMockKTest {

    private val testDispatcher = StandardTestDispatcher()

    private val syncCitiesUseCase = mockk<SyncCitiesUseCase>(relaxed = true)
    private val getCitiesUseCase = mockk<GetCitiesUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>(relaxed = true)
    private val getFavoriteIdsUseCase = mockk<GetFavoriteIdsUseCase>()
    private val getCityByIdUseCase = mockk<GetCityByIdUseCase>()

    private lateinit var viewModel: CityViewModel

    val sampleCities = listOf(
        City(id = 1, name = "Barcelona", country = "ES", lat = 41.3851, lon = 2.1734, isFavorite = false),
        City(id = 2, name = "Berlin", country = "DE", lat = 52.52, lon = 13.405, isFavorite = false),
        City(id = 3, name = "Boston", country = "US", lat = 42.3601, lon = -71.0589, isFavorite = false),
        City(id = 4, name = "Amsterdam", country = "NL", lat = 52.3676, lon = 4.9041, isFavorite = false),
        City(id = 5, name = "Sydney", country = "AU", lat = -33.8688, lon = 151.2093, isFavorite = false),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search returns filtered cities ignoring case`() = runTest {
        every { getCitiesUseCase() } returns flowOf(NetworkResult.Success(sampleCities))
        every { getFavoriteIdsUseCase() } returns flowOf(emptySet())

        viewModel = CityViewModel(
            syncCitiesUseCase,
            getCitiesUseCase,
            toggleFavoriteUseCase,
            getFavoriteIdsUseCase,
            getCityByIdUseCase
        )

        viewModel.onSearchQueryChanged("b")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filteredCities.test {
            var result = awaitItem()
            while (result !is NetworkResult.Success) {
                result = awaitItem()
            }
            val filteredCities = result.data

            println("Ciudades filtradas: ${filteredCities.map { it.name }}")

            filteredCities.forEach { city ->
                assertTrue(
                    "La ciudad '${city.name}' no empieza con 'B' (ignoreCase=true)",
                    city.name.startsWith("B", ignoreCase = true)
                )
            }
        }
    }

    @Test
    fun `search with empty query returns all cities`() = runTest {
        val favoriteIds = emptySet<Int>()

        coEvery { getCitiesUseCase.invoke() } returns flowOf(NetworkResult.Success(sampleCities))
        coEvery { getFavoriteIdsUseCase.invoke() } returns flowOf(favoriteIds)

        viewModel = CityViewModel(
            syncCitiesUseCase,
            getCitiesUseCase,
            toggleFavoriteUseCase,
            getFavoriteIdsUseCase,
            getCityByIdUseCase
        )

        viewModel.onSearchQueryChanged("")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filteredCities.test {
            var result = awaitItem()
            while (result !is NetworkResult.Success) {
                result = awaitItem()
            }
            val filtered = result.data
            assertEquals(sampleCities.size, filtered.size)
        }
    }

    @Test
    fun `filter only favorites shows only favorite cities matching query`() = runTest {
        val favoriteIds = setOf(2, 3) // Berlin y Boston favoritos

        coEvery { getCitiesUseCase.invoke() } returns flowOf(NetworkResult.Success(sampleCities))
        coEvery { getFavoriteIdsUseCase.invoke() } returns flowOf(favoriteIds)

        viewModel = CityViewModel(
            syncCitiesUseCase,
            getCitiesUseCase,
            toggleFavoriteUseCase,
            getFavoriteIdsUseCase,
            getCityByIdUseCase
        )

        viewModel.onSearchQueryChanged("B")  // busca ciudades que empiezan con B
        viewModel.onToggleOnlyFavorites()    // activa el filtro "solo favoritos"
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filteredCities.test {
            var result = awaitItem()
            while (result !is NetworkResult.Success) {
                result = awaitItem()
            }
            val filtered = result.data

            // Solo Berlin y Boston son favoritos y empiezan con B
            assertEquals(2, filtered.size)
            assertTrue(filtered.all { it.isFavorite })
            assertTrue(filtered.all { it.name.startsWith("B", ignoreCase = true) })
        }
    }
}

