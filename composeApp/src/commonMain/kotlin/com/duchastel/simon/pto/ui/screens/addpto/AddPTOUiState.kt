package com.duchastel.simon.pto.ui.screens.addpto

import com.slack.circuit.runtime.CircuitUiState

sealed interface AddPTOUiState : CircuitUiState {
    data object Loading : AddPTOUiState

    data class Loaded(
        val nextXDaysInput: String,
        val isValidNextXDays: Boolean,
        val onNextXDaysChanged: (String) -> Unit,
        val onAddToday: () -> Unit,
        val onAddNextXDays: () -> Unit,
        val onViewAllDays: () -> Unit,
        val onBack: () -> Unit
    ) : AddPTOUiState
}
