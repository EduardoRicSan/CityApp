package com.tablegroup.ualaapptest.navigation

import android.Manifest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale


/**
 * Sealed class representing navigation screens with their routes.
 */
sealed class Screen(val route: String) {
    /** Screen showing the list of cities. */
    data object CityList : Screen("city_list")

    /**
     * Screen showing a map for a specific city identified by cityId.
     *
     * @param cityId The ID of the city to display on the map.
     * @return The concrete route with the city ID included.
     */
    data object CityMap : Screen("map/{cityId}") {
        fun createRoute(cityId: Int): String = "map/$cityId"
    }
}

@OptIn(ExperimentalPermissionsApi::class)
/**
 * Composable that requests location permission and shows content if granted.
 *
 * @param onPermissionGranted Composable content to show when permission is granted.
 */
@Composable
fun RequestLocationPermission(onPermissionGranted: @Composable () -> Unit) {
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    /**
     * Request location permission when this composable enters composition.
     */
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    when {
        locationPermissionState.status.isGranted -> {
            onPermissionGranted()
        }
        locationPermissionState.status.shouldShowRationale -> {
            Text("The app needs your location to show the map.")
        }
        else -> {
            Text("Permission denied. Please enable it in settings.")
        }
    }
}