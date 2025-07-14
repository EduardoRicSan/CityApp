package com.tablegroup.ualaapptest

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.tablegroup.ualaapptest.navigation.AppNavigation
import com.tablegroup.ualaapptest.ui.composables.CityListMapScreen
import com.tablegroup.ualaapptest.ui.theme.UalaAppTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UalaAppTestTheme {
               UalaMainContent()
            }
        }
    }

    @Composable
    fun UalaMainContent() {
        val configuration = LocalConfiguration.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { },
            bottomBar = { },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPaddings ->
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                CityListMapScreen()
            } else {
                AppNavigation(modifier = Modifier.padding(innerPaddings))
            }
        }
    }
}
