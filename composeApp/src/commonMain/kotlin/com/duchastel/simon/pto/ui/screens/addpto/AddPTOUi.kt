package com.duchastel.simon.pto.ui.screens.addpto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddPTOUi(state: AddPTOUiState, modifier: Modifier = Modifier) {
    when (state) {
        is AddPTOUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AddPTOUiState.Loaded -> {
            AddPTOLoadedContent(state, modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPTOLoadedContent(state: AddPTOUiState.Loaded, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Add PTO") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Quick Add",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = state.onAddToday,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Add Today", fontSize = 18.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Add Multiple Days",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = state.nextXDaysInput,
                    onValueChange = state.onNextXDaysChanged,
                    label = { Text("Days") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.nextXDaysInput.isNotEmpty() && !state.isValidNextXDays,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = state.onAddNextXDays,
                    enabled = state.isValidNextXDays,
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Text("Add Next X Days")
                }
            }

            if (state.nextXDaysInput.isNotEmpty() && !state.isValidNextXDays) {
                Text(
                    text = "Please enter a valid number",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = state.onViewAllDays,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("View All PTO Days", fontSize = 18.sp)
            }
        }
    }
}
