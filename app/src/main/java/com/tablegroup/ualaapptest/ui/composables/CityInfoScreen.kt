package com.tablegroup.ualaapptest.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.designsystem.loader.SimpleLoader
import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.UIWeather
import com.tablegroup.ualaapptest.R
import com.tablegroup.ualaapptest.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInfoScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    city: City,
    onBack: () -> Unit
) {

    LaunchedEffect(city) {
        weatherViewModel.getWeatherByCity(city.name)
    }

    when (val weatherState = weatherViewModel.weatherCity.collectAsStateWithLifecycle().value) {
        is NetworkResult.Loading -> {
           SimpleLoader()
        }
        is NetworkResult.Error -> {
            Log.d("INFO ERROR", "${weatherState.message}")
        }
        is NetworkResult.Success -> {
            CityInfoContent(weatherState.data, onBack)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInfoContent(uiWeather: UIWeather, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiWeather.city) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.label_full_info, uiWeather.fullRegion), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.label_latest_updated, uiWeather.localTime), style = MaterialTheme.typography.bodyMedium)
            WeatherIcon(uiWeather.icon)
            Text(stringResource(R.string.label_condition, uiWeather.condition), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.label_wind, uiWeather.windKph), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.label_clouds, uiWeather.cloud.toString().plus("%")), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(R.string.label_humidity, uiWeather.humidity.toString().plus("%")), style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Composable
fun WeatherIcon(iconPath: String) {
    val imageUrl = if (iconPath.startsWith("//")) {
        "https:$iconPath"
    } else {
        iconPath
    }
    AsyncImage(
        model = imageUrl,
        contentDescription = "Weather Icon",
        modifier = Modifier.size(100.dp),
        contentScale = ContentScale.Fit
    )
}