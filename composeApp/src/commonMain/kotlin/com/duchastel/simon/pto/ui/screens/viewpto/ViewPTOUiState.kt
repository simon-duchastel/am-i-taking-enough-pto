package com.duchastel.simon.pto.ui.screens.viewpto

import com.duchastel.simon.pto.domain.models.PTODay
import com.slack.circuit.runtime.CircuitUiState

sealed interface ViewPTOUiState : CircuitUiState {
    data object Loading : ViewPTOUiState

    data class Loaded(
        val ptoDays: List<PTODay>,
        val viewMode: ViewMode,
        val onToggleViewMode: () -> Unit,
        val onDeleteDay: (PTODay) -> Unit,
        val onBack: () -> Unit
    ) : ViewPTOUiState
}

enum class ViewMode {
    LIST,
    CALENDAR
}
