package com.duchastel.simon.pto.ui.navigation

import com.duchastel.simon.pto.parcel.Parcelize
import com.slack.circuit.runtime.screen.Screen
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
