package com.duchastel.simon.pto.ui.screens.home

import com.duchastel.simon.pto.domain.models.PTOStatus
import com.duchastel.simon.pto.domain.models.YearMode
import com.slack.circuit.runtime.CircuitUiState

sealed interface HomeUiState : CircuitUiState {
    data object Loading : HomeUiState

    data class Loaded(
        val daysTaken: Int,
        val targetDays: Int,
        val status: PTOStatus,
        val yearMode: YearMode,
        val onToggleYearMode: () -> Unit,
        val onAddPTO: () -> Unit,
        val onViewPTO: () -> Unit,
        val onSettings: () -> Unit
    ) : HomeUiState
}
