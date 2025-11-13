package com.duchastel.simon.pto.ui.screens.settings

import androidx.compose.runtime.*
import com.duchastel.simon.pto.domain.repository.SettingsRepository
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch

class SettingsPresenter(
    private val navigator: Navigator,
    private val settingsRepository: SettingsRepository
) : Presenter<SettingsUiState> {

    @Composable
    override fun present(): SettingsUiState {
        val scope = rememberCoroutineScope()
        val settings by settingsRepository.getSettings().collectAsState(initial = null)

        var targetInput by remember { mutableStateOf("") }

        LaunchedEffect(settings) {
            settings?.let {
                if (targetInput.isEmpty()) {
                    targetInput = it.targetPTODays.toString()
                }
            }
        }

        if (settings == null) {
            return SettingsUiState.Loading
        }

        val targetValue = targetInput.toIntOrNull()
        val isValid = targetValue != null && targetValue > 0

        return SettingsUiState.Loaded(
            targetPTODays = settings!!.targetPTODays,
            targetPTODaysInput = targetInput,
            isValidInput = isValid,
            onTargetPTODaysChanged = { input ->
                targetInput = input
            },
            onSave = {
                if (isValid) {
                    scope.launch {
                        settingsRepository.updateTargetPTODays(targetValue!!)
                        navigator.pop()
                    }
                }
            },
            onBack = {
                navigator.pop()
            }
        )
    }
}
