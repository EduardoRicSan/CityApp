package com.tablegroup.ualaapptest.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.toCity
import com.tablegroup.domain.model.toMap
import com.tablegroup.ualaapptest.navigation.RequestLocationPermission
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel

@Composable
fun CityListMapScreen(
    viewModel: CityViewModel,
    modifier: Modifier = Modifier,
    onInfoClick: (City) -> Unit
) {
    val selectedCity = rememberSaveable(stateSaver = NullableCitySaver) {
        mutableStateOf<City?>(null)
    }

    Row(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            CityListScreen(
                viewModel = viewModel,
                onCityClick = { city -> selectedCity.value = city },
                onInfoClick = onInfoClick
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
    save = { city -> city.toMap() },
    restore = { map -> map.toCity() }
)