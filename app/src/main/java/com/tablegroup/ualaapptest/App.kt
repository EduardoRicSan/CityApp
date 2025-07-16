package com.tablegroup.ualaapptest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class setup for Hilt dependency injection.
 * Enables Hilt code generation for the app.
 */
@HiltAndroidApp
class App : Application()
