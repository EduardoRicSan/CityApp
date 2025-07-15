package com.tablegroup.ualaapptest.navigation

import android.Manifest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale


sealed class Screen(val route: String) {
    data object CityList : Screen("city_list")
    data object CityMap : Screen("map/{cityId}") {
        fun createRoute(cityId: Int): String = "map/$cityId"
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: @Composable () -> Unit) {
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    when {
        locationPermissionState.status.isGranted -> {
            onPermissionGranted()
        }
        locationPermissionState.status.shouldShowRationale -> {
            Text("La app necesita tu ubicación para mostrar el mapa.")
        }
        else -> {
            Text("Permiso denegado. Ve a ajustes para habilitarlo.")
        }
    }
}