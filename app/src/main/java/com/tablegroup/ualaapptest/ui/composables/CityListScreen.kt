package com.tablegroup.ualaapptest.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel

@Composable
fun CityListScreen(
    viewModel: CityViewModel,
    onCityClick: (City) -> Unit
) {
    val citiesResult by viewModel.filteredCities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val onlyFavorites by viewModel.onlyFavorites.collectAsState()

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

        when (citiesResult) {
            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NetworkResult.Error -> {
                Text(
                    text = "Error loading cities",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is NetworkResult.Success -> {
                Log.d("CITY", "${(citiesResult as NetworkResult.Success<List<City>>).data}")
                CityList(
                    cities = (citiesResult as NetworkResult.Success<List<City>>).data,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onCityClicked = onCityClick
                )
            }
        }
    }
}

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

@Composable
fun CityList(
    cities: List<City>,
    onToggleFavorite: (Int) -> Unit,
    onCityClicked: (City) -> Unit
) {
    LazyColumn {
        items(cities) { city ->
            CityRow(city = city, onToggleFavorite = onToggleFavorite, onCityClicked = onCityClicked)
        }
    }
}

@Composable
fun CityRow(
    city: City,
    onToggleFavorite: (Int) -> Unit,
    onCityClicked: (City) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCityClicked(city) },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "${city.name}, ${city.country}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Lat: ${city.lat}, Lon: ${city.lon}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onToggleFavorite(city.id) }) {
                Icon(
                    imageVector = if (city.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Toggle Favorite"
                )
            }
        }
    }
}