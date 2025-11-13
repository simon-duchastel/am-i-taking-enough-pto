package com.duchastel.simon.pto.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.duchastel.simon.pto.domain.models.PTOSummary
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.duchastel.simon.pto.domain.repository.SettingsRepository
import com.duchastel.simon.pto.ui.navigation.AddPTOScreen
import com.duchastel.simon.pto.ui.navigation.HomeScreen
import com.duchastel.simon.pto.ui.navigation.SettingsScreen
import com.duchastel.simon.pto.ui.navigation.ViewPTOScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomePresenter(
    private val navigator: Navigator,
    private val ptoRepository: PTORepository,
    private val settingsRepository: SettingsRepository
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        val settings by settingsRepository.getSettings().collectAsState(initial = null)
        val ptoDays by ptoRepository.getAllPTODays().collectAsState(initial = null)

        if (settings == null || ptoDays == null) {
            return HomeUiState.Loading
        }

        val currentSettings = settings!!
        val currentPTODays = ptoDays!!

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val yearMode = currentSettings.yearMode

        val relevantDays = currentPTODays.filter { ptoDay ->
            val date = ptoDay.date
            when (yearMode) {
                com.ptotracker.domain.models.YearMode.CALENDAR_YEAR -> {
                    date.year == now.year
                }
                com.ptotracker.domain.models.YearMode.ROLLING_365_DAYS -> {
                    val daysDiff = date.toEpochDays() - now.date.toEpochDays()
                    daysDiff >= -365 && daysDiff <= 0
                }
            }
        }

        val summary = PTOSummary.calculate(
            daysTaken = relevantDays.size,
            targetDays = currentSettings.targetPTODays
        )

        return HomeUiState.Loaded(
            daysTaken = summary.daysTaken,
            targetDays = summary.targetDays,
            status = summary.status,
            yearMode = currentSettings.yearMode,
            onToggleYearMode = {
                scope.launch {
                    val newMode = when (currentSettings.yearMode) {
                        com.ptotracker.domain.models.YearMode.CALENDAR_YEAR ->
                            com.ptotracker.domain.models.YearMode.ROLLING_365_DAYS
                        com.ptotracker.domain.models.YearMode.ROLLING_365_DAYS ->
                            com.ptotracker.domain.models.YearMode.CALENDAR_YEAR
                    }
                    settingsRepository.updateYearMode(newMode)
                }
            },
            onAddPTO = { navigator.goTo(AddPTOScreen) },
            onViewPTO = { navigator.goTo(ViewPTOScreen) },
            onSettings = { navigator.goTo(SettingsScreen) }
        )
    }
}
