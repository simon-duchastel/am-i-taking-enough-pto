package com.duchastel.simon.pto.ui.screens.settings

import com.slack.circuit.runtime.CircuitUiState

sealed interface SettingsUiState : CircuitUiState {
    data object Loading : SettingsUiState

    data class Loaded(
        val targetPTODays: Int,
        val targetPTODaysInput: String,
        val isValidInput: Boolean,
        val onTargetPTODaysChanged: (String) -> Unit,
        val onSave: () -> Unit,
        val onBack: () -> Unit
    ) : SettingsUiState
}
