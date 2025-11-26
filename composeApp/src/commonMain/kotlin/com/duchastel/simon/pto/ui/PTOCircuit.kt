package com.duchastel.simon.pto.ui

import androidx.compose.runtime.Composable
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.duchastel.simon.pto.domain.repository.SettingsRepository
import com.duchastel.simon.pto.ui.navigation.AddPTOScreen
import com.duchastel.simon.pto.ui.navigation.HomeScreen
import com.duchastel.simon.pto.ui.navigation.SettingsScreen
import com.duchastel.simon.pto.ui.navigation.ViewPTOScreen
import com.duchastel.simon.pto.ui.screens.addpto.AddPTOPresenter
import com.duchastel.simon.pto.ui.screens.addpto.AddPTOUi
import com.duchastel.simon.pto.ui.screens.addpto.AddPTOUiState
import com.duchastel.simon.pto.ui.screens.home.HomePresenter
import com.duchastel.simon.pto.ui.screens.home.HomeUi
import com.duchastel.simon.pto.ui.screens.home.HomeUiState
import com.duchastel.simon.pto.ui.screens.settings.SettingsPresenter
import com.duchastel.simon.pto.ui.screens.settings.SettingsUi
import com.duchastel.simon.pto.ui.screens.settings.SettingsUiState
import com.duchastel.simon.pto.ui.screens.viewpto.ViewPTOPresenter
import com.duchastel.simon.pto.ui.screens.viewpto.ViewPTOUi
import com.duchastel.simon.pto.ui.screens.viewpto.ViewPTOUiState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

class PTOCircuitFactory(
    private val ptoRepository: PTORepository,
    private val settingsRepository: SettingsRepository
) {
    fun create(): Circuit {
        return Circuit.Builder()
            .addPresenterFactory { screen, navigator, _ ->
                createPresenter(screen, navigator)
            }
            .addUiFactory { screen, _ ->
                createUi(screen)
            }
            .build()
    }

    private fun createPresenter(screen: Screen, navigator: Navigator): Presenter<*>? {
        return when (screen) {
            is HomeScreen -> HomePresenter(navigator, ptoRepository, settingsRepository)
            is AddPTOScreen -> AddPTOPresenter(navigator, ptoRepository)
            is ViewPTOScreen -> ViewPTOPresenter(navigator, ptoRepository)
            is SettingsScreen -> SettingsPresenter(navigator, settingsRepository)
            else -> null
        }
    }

    private fun createUi(screen: Screen): Ui<*>? {
        return when (screen) {
            is HomeScreen -> ui<HomeUiState> { state, modifier ->
                HomeUi(state, modifier)
            }
            is AddPTOScreen -> ui<AddPTOUiState> { state, modifier ->
                AddPTOUi(state, modifier)
            }
            is ViewPTOScreen -> ui<ViewPTOUiState> { state, modifier ->
                ViewPTOUi(state, modifier)
            }
            is SettingsScreen -> ui<SettingsUiState> { state, modifier ->
                SettingsUi(state, modifier)
            }
            else -> null
        }
    }
}

@Composable
fun PTOApp(circuit: Circuit) {
    CircuitCompositionLocals(circuit) {
        val backStack = rememberSaveableBackStack(root = HomeScreen)
        val navigator = rememberCircuitNavigator(
            backStack = backStack,
            onRootPop = { }, // no-op root pop
        )
        NavigableCircuitContent(
            navigator = navigator,
            backStack = backStack
        )
    }
}
