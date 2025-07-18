package com.tablegroup.ualaapptest.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.designsystem.loader.SkeletonLoader
import com.tablegroup.domain.model.City
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel

/**
 * Displays a list of cities with search and favorites filtering.
 *
 * Observes ViewModel state and renders loading, error, or city list accordingly.
 */
@Composable
fun CityListScreen(
    viewModel: CityViewModel,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    val citiesResult by viewModel.filteredCities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val onlyFavorites by viewModel.onlyFavorites.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = viewModel::onSearchQueryChanged,
            onlyFavorites = onlyFavorites,
            onToggleFavorites = viewModel::onToggleOnlyFavorites
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isSyncing) {
            SkeletonLoader()
        } else {
            when (citiesResult) {
                is NetworkResult.Loading -> SkeletonLoader()
                is NetworkResult.Error -> Text(
                    text = "Error loading cities",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                is NetworkResult.Success -> {
                    val cities = (citiesResult as NetworkResult.Success<List<City>>).data
                    if (cities.isEmpty()) {
                        Text(
                            text = "No cities found",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        CityList(
                            cities = cities,
                            onToggleFavorite = viewModel::toggleFavorite,
                            onCityClicked = onCityClick,
                            onInfoClick = onInfoClick
                        )
                    }
                }
            }
        }
    }
}


/**
 * Search bar with text input and favorites filter toggle button.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onlyFavorites: Boolean,
    onToggleFavorites: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Search city") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onToggleFavorites) {
            Icon(
                imageVector = if (onlyFavorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (onlyFavorites) "Show all cities" else "Show favorites only"
            )
        }
    }
}

/**
 * LazyColumn showing a list of cities as rows.
 */
@Composable
fun CityList(
    cities: List<City>,
    onToggleFavorite: (Int) -> Unit,
    onCityClicked: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    LazyColumn {
        items(cities) { city ->
            CityRow(
                city = city,
                onToggleFavorite = onToggleFavorite,
                onCityClicked = onCityClicked,
                onInfoClick = onInfoClick
            )
        }
    }
}

/**
 * Single row representing a city with name, coordinates, favorite toggle, and info button.
 */
@Composable
fun CityRow(
    city: City,
    onToggleFavorite: (Int) -> Unit,
    onCityClicked: (City) -> Unit,
    onInfoClick: (City) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onCityClicked(city)
                },
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "${city.name}, ${city.country}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Lat: ${city.lat}, Lon: ${city.lon}", style = MaterialTheme.typography.bodyMedium)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { onToggleFavorite(city.id) }) {
                    Icon(
                        imageVector = if (city.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle Favorite"
                    )
                }
                IconButton(onClick = { onInfoClick(city) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Info"
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

/**
 * Previews
 */

// Dummy data para preview
val sampleCity = City(id = 1, name = "Madrid", country = "ES", lat = 40.4168, lon = -3.7038, isFavorite = true)
val sampleCities = listOf(
    sampleCity,
    City(id = 2, name = "Paris", country = "FR", lat = 48.8566, lon = 2.3522, isFavorite = false)
)

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    SearchBar(
        query = "Madrid",
        onQueryChanged = {},
        onlyFavorites = true,
        onToggleFavorites = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityList() {
    CityList(
        cities = sampleCities,
        onToggleFavorite = {},
        onCityClicked = {},
        onInfoClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityRow() {
    CityRow(
        city = sampleCity,
        onToggleFavorite = {},
        onCityClicked = {},
        onInfoClick = {}
    )
}
