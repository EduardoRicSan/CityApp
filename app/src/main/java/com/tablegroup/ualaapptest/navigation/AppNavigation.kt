package com.tablegroup.ualaapptest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.tablegroup.domain.model.City
import com.tablegroup.ualaapptest.ui.composables.CityInfoScreen
import com.tablegroup.ualaapptest.ui.composables.CityListMapScreen
import com.tablegroup.ualaapptest.ui.composables.CityListScreen
import com.tablegroup.ualaapptest.ui.composables.CityMapScreen
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel


/**
 * Defines the navigation graph for the app using Jetpack Compose Navigation.
 *
 * Handles different UI flows depending on orientation (landscape or portrait).
 * - In landscape mode, shows a combined city list and map screen.
 * - In portrait mode, navigates between city list, city map, and city info screens.
 *
 * @param viewModel Shared [CityViewModel] instance provided by Hilt.
 * @param modifier Modifier for styling the NavHost.
 * @param landscapeMode Flag indicating if the device is in landscape orientation.
 */
@Composable
fun AppNavigation(
    viewModel: CityViewModel = hiltViewModel(),
    modifier: Modifier,
    landscapeMode: Boolean = false,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.CityList.route,
        modifier = modifier
    ) {
        // Main screen showing city list and (optionally) map depending on orientation
        composable(Screen.CityList.route) {
            if (landscapeMode) {
                CityListMapScreen(
                    onInfoClick = { city ->
                        navController.navigate(city)  // Navigate to city info using custom extension
                    },
                    viewModel = viewModel
                )
            } else {
                CityListScreen(
                    viewModel = viewModel,
                    onCityClick = { city ->
                        navController.navigate(Screen.CityMap.createRoute(city.id))  // Navigate to map screen with city id
                    },
                    onInfoClick = { city ->
                        navController.navigate(city)  // Navigate to city info screen
                    }
                )
            }
        }

        // City map screen, expects cityId argument from nav route
        composable(
            route = Screen.CityMap.route,
            arguments = listOf(navArgument("cityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId") ?: return@composable
            CityMapScreen(
                cityId = cityId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }  // Navigate back on UI action
            )
        }

        // City info screen accepting a City object via type-safe navigation
        composable<City> { backStackEntry ->
            val city: City = backStackEntry.toRoute()
            CityInfoScreen(
                city = city,
                onBack = { navController.popBackStack() }  // Navigate back on UI action
            )
        }
    }
}

