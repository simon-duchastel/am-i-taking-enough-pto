package com.duchastel.simon.pto.ui.navigation

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data object HomeScreen : Screen

@Parcelize
@Serializable
data object AddPTOScreen : Screen

@Parcelize
@Serializable
data object ViewPTOScreen : Screen

@Parcelize
@Serializable
data object SettingsScreen : Screen
