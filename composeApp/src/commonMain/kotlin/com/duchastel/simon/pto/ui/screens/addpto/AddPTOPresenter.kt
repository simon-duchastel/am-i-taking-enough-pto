package com.duchastel.simon.pto.ui.screens.addpto

import androidx.compose.runtime.*
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.duchastel.simon.pto.ui.navigation.ViewPTOScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class AddPTOPresenter(
    private val navigator: Navigator,
    private val ptoRepository: PTORepository
) : Presenter<AddPTOUiState> {

    @Composable
    override fun present(): AddPTOUiState {
        val scope = rememberCoroutineScope()
        var nextXDaysInput by remember { mutableStateOf("") }

        val nextXDaysValue = nextXDaysInput.toIntOrNull()
        val isValidNextXDays = nextXDaysValue != null && nextXDaysValue > 0

        return AddPTOUiState.Loaded(
            nextXDaysInput = nextXDaysInput,
            isValidNextXDays = isValidNextXDays,
            onNextXDaysChanged = { input ->
                nextXDaysInput = input
            },
            onAddToday = {
                scope.launch {
                    val today = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                    ptoRepository.addPTODay(today)
                    navigator.pop()
                }
            },
            onAddNextXDays = {
                if (isValidNextXDays) {
                    scope.launch {
                        val today = Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date

                        val dates = (0 until nextXDaysValue!!).map { offset ->
                            today.plus(offset, DateTimeUnit.DAY)
                        }

                        ptoRepository.addPTODays(dates)
                        navigator.pop()
                    }
                }
            },
            onViewAllDays = {
                navigator.goTo(ViewPTOScreen)
            },
            onBack = {
                navigator.pop()
            }
        )
    }
}
