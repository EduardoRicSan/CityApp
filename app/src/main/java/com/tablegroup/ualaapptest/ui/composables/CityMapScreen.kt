package com.tablegroup.ualaapptest.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tablegroup.domain.model.City
import com.tablegroup.ualaapptest.navigation.RequestLocationPermission
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel

/**
 * Screen displaying a map centered on a city by its ID.
 * Fetches city from ViewModel and shows loading until ready.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    cityId: Int,
    viewModel: CityViewModel,
    onBack: () -> Unit
) {
    // Holds the city data loaded from the ViewModel
    var city by remember { mutableStateOf<City?>(null) }

    // Load city when cityId changes
    LaunchedEffect(cityId) {
        city = viewModel.getCityById(cityId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(city?.name ?: "City Map") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (city != null) {
            // Request location permission and show map if granted
            RequestLocationPermission {
                CityMap(city = city!!, modifier = Modifier.padding(padding))
            }
        } else {
            // Show loading indicator while city is null
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * Map composable showing a marker at the city's coordinates.
 * Animates camera to city's location on city change.
 */
@Composable
fun CityMap(city: City, modifier: Modifier = Modifier) {
    val cameraPositionState = rememberCameraPositionState()

    // Animate camera zoom and position when city changes
    LaunchedEffect(city) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(city.lat, city.lon),
                10f
            )
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(city.lat, city.lon)),
            title = city.name,
            snippet = city.country
        )
    }
}
