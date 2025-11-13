package com.duchastel.simon.pto.ui.navigation

import com.slack.circuit.runtime.screen.Screen
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen : Screen

@Serializable
data object AddPTOScreen : Screen

@Serializable
data object ViewPTOScreen : Screen

@Serializable
data object SettingsScreen : Screen
