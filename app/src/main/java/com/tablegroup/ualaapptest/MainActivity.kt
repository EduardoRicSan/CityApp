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
import com.tablegroup.ualaapptest.ui.theme.UalaAppTestTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the app with Hilt injection enabled.
 * Sets up edge-to-edge display and Compose content.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge UI
        setContent {
            UalaAppTestTheme {
                UalaMainContent()
            }
        }
    }

    /**
     * Main composable content hosting the app navigation.
     * Adapts layout based on device orientation.
     */
    @Composable
    fun UalaMainContent() {
        val configuration = LocalConfiguration.current
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { /* Optional top bar can be added here */ },
            bottomBar = { /* Optional bottom bar can be added here */ },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPaddings ->
            AppNavigation(
                modifier = Modifier.padding(innerPaddings),
                landscapeMode = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            )
        }
    }
}

