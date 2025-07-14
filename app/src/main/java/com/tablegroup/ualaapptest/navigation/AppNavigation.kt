package com.tablegroup.ualaapptest.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tablegroup.ualaapptest.ui.composables.CityListMapScreen
import com.tablegroup.ualaapptest.ui.composables.CityListScreen
import com.tablegroup.ualaapptest.ui.composables.CityMapScreen
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel



@Composable
fun AppNavigation(
    viewModel: CityViewModel = hiltViewModel(),
    modifier: Modifier,
    ) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.CityList.route,
        modifier = modifier
    ) {

        // Lista de ciudades
        composable(Screen.CityList.route) {
            CityListScreen(
                viewModel = viewModel,
                onCityClick = { city ->
                    navController.navigate(Screen.CityMap.createRoute(city.id))
                }
            )
        }

        // Pantalla del mapa
        composable(
            route = Screen.CityMap.route,
            arguments = listOf(navArgument("cityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId") ?: return@composable
            CityMapScreen(
                cityId = cityId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}