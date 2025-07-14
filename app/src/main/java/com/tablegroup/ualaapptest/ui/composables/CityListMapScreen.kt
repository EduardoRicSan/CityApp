package com.tablegroup.ualaapptest.ui.composables

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tablegroup.domain.model.City
import com.tablegroup.ualaapptest.navigation.RequestLocationPermission
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel

@Composable
fun CityListMapScreen(
    viewModel: CityViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val selectedCity = rememberSaveable(stateSaver = NullableCitySaver) {
        mutableStateOf<City?>(null)
    }

    Row(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            CityListScreen(
                viewModel = viewModel,
                onCityClick = { city -> selectedCity.value = city }
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box(modifier = Modifier.weight(1f)) {
            selectedCity.value?.let { city ->
                RequestLocationPermission {
                    CityMap(city = city)
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Select a city to view on map")
            }
        }
    }
}


val NullableCitySaver = Saver<City?, Map<String, Any>>(
    save = { city ->
        city?.let {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "country" to it.country,
                "lat" to it.lat,
                "lon" to it.lon,
                "isFavorite" to it.isFavorite
            )
        } ?: emptyMap()
    },
    restore = { map ->
        if (map.isEmpty()) null else City(
            id = map["id"] as Int,
            name = map["name"] as String,
            country = map["country"] as String,
            lat = map["lat"] as Double,
            lon = map["lon"] as Double,
            isFavorite = map["isFavorite"] as Boolean
        )
    }
)