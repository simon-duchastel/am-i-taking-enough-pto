package com.duchastel.simon.pto.ui.screens.viewpto

import androidx.compose.runtime.*
import com.duchastel.simon.pto.domain.models.PTODay
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch

class ViewPTOPresenter(
    private val navigator: Navigator,
    private val ptoRepository: PTORepository
) : Presenter<ViewPTOUiState> {

    @Composable
    override fun present(): ViewPTOUiState {
        val scope = rememberCoroutineScope()
        val ptoDays by ptoRepository.getAllPTODays().collectAsState(initial = null)
        var viewMode by remember { mutableStateOf(ViewMode.LIST) }

        if (ptoDays == null) {
            return ViewPTOUiState.Loading
        }

        return ViewPTOUiState.Loaded(
            ptoDays = ptoDays!!.sortedByDescending { it.date },
            viewMode = viewMode,
            onToggleViewMode = {
                viewMode = when (viewMode) {
                    ViewMode.LIST -> ViewMode.CALENDAR
                    ViewMode.CALENDAR -> ViewMode.LIST
                }
            },
            onDeleteDay = { day ->
                scope.launch {
                    ptoRepository.removePTODay(day.date)
                }
            },
            onBack = {
                navigator.pop()
            }
        )
    }
}
