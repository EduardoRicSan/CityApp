package com.tablegroup.ualaapptest

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tablegroup.ualaapptest.ui.composables.CityListMapScreen
import com.tablegroup.ualaapptest.ui.viewmodel.CityViewModel
import io.mockk.mockk

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CityListMapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun cityListMapScreen_showsSelectCityMessage_initially() {
        val viewModel = mockk<CityViewModel>(relaxed = true)

        composeTestRule.setContent {
            CityListMapScreen(
                viewModel = viewModel,
                onInfoClick = {}
            )
        }

        composeTestRule.onNodeWithText("Select a city to view on map").assertIsDisplayed()
    }
}