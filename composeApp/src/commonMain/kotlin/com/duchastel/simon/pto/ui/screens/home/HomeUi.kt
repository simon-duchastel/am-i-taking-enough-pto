package com.duchastel.simon.pto.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duchastel.simon.pto.domain.models.PTOStatus
import com.duchastel.simon.pto.domain.models.YearMode

@Composable
fun HomeUi(state: HomeUiState, modifier: Modifier = Modifier) {
    when (state) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeUiState.Loaded -> {
            HomeLoadedContent(state, modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeLoadedContent(state: HomeUiState.Loaded, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("PTO Tracker") },
                actions = {
                    TextButton(onClick = state.onToggleYearMode) {
                        Text(
                            text = when (state.yearMode) {
                                YearMode.CALENDAR_YEAR -> "Calendar Year"
                                YearMode.ROLLING_365_DAYS -> "Rolling 365 Days"
                            },
                            fontSize = 12.sp
                        )
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = when (state.status) {
                            PTOStatus.GOOD -> Color(0xFF4CAF50)
                            PTOStatus.WARNING -> Color(0xFFFFC107)
                            PTOStatus.CRITICAL -> Color(0xFFF44336)
                        },
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${state.daysTaken}",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "days taken",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Target: ${state.targetDays} days",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                if (state.yearMode == YearMode.CALENDAR_YEAR) {
                    val expectedByNow = (state.targetDays * state.yearProgress).toInt()
                    Text(
                        text = "Expected by now: $expectedByNow days (${(state.yearProgress * 100).toInt()}% of year)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val percentage = if (state.targetDays > 0) {
                (state.daysTaken.toFloat() / state.targetDays.toFloat() * 100).toInt()
            } else {
                0
            }

            LinearProgressIndicator(
                progress = { (state.daysTaken.toFloat() / state.targetDays.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = when (state.status) {
                    PTOStatus.GOOD -> Color(0xFF4CAF50)
                    PTOStatus.WARNING -> Color(0xFFFFC107)
                    PTOStatus.CRITICAL -> Color(0xFFF44336)
                },
            )

            val statusMessage = when {
                state.yearMode == YearMode.ROLLING_365_DAYS ->
                    "$percentage% of target"
                else -> {
                    val expectedByNow = (state.targetDays * state.yearProgress).toInt()
                    when (state.status) {
                        PTOStatus.GOOD -> "✓ On track or ahead of schedule"
                        PTOStatus.WARNING -> "⚠ Slightly behind schedule"
                        PTOStatus.CRITICAL -> "⚠ Behind schedule - time to book some PTO!"
                    }
                }
            }

            Text(
                text = statusMessage,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (state.status == PTOStatus.CRITICAL) FontWeight.Medium else FontWeight.Normal
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = state.onAddPTO,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Add PTO", fontSize = 18.sp)
            }

            OutlinedButton(
                onClick = state.onViewPTO,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("View PTO Days", fontSize = 18.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = state.onSettings) {
                    Text("⚙️ Settings", fontSize = 14.sp)
                }
            }
        }
    }
}
