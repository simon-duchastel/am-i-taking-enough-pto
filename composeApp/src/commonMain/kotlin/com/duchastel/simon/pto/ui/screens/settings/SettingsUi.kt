package com.duchastel.simon.pto.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SettingsUi(state: SettingsUiState, modifier: Modifier = Modifier) {
    when (state) {
        is SettingsUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SettingsUiState.Loaded -> {
            SettingsLoadedContent(state, modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsLoadedContent(state: SettingsUiState.Loaded, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    TextButton(onClick = state.onBack) {
                        Text("← Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Target PTO Days",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = state.targetPTODaysInput,
                onValueChange = state.onTargetPTODaysChanged,
                label = { Text("Target Days") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = !state.isValidInput,
                supportingText = {
                    if (!state.isValidInput) {
                        Text("Please enter a valid number greater than 0")
                    } else {
                        Text("Number of PTO days you aim to take per year")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = state.onSave,
                enabled = state.isValidInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save")
            }
        }
    }
}
