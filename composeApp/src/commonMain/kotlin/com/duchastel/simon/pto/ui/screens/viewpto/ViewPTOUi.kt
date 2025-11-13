package com.duchastel.simon.pto.ui.screens.viewpto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duchastel.simon.pto.domain.models.PTODay
import kotlinx.datetime.LocalDate

@Composable
fun ViewPTOUi(state: ViewPTOUiState, modifier: Modifier = Modifier) {
    when (state) {
        is ViewPTOUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ViewPTOUiState.Loaded -> {
            ViewPTOLoadedContent(state, modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewPTOLoadedContent(state: ViewPTOUiState.Loaded, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("PTO Days") },
                navigationIcon = {
                    TextButton(onClick = state.onBack) {
                        Text("← Back")
                    }
                },
                actions = {
                    TextButton(onClick = state.onToggleViewMode) {
                        Text(
                            when (state.viewMode) {
                                ViewMode.LIST -> "Calendar View"
                                ViewMode.CALENDAR -> "List View"
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (state.viewMode) {
            ViewMode.LIST -> {
                PTOListView(
                    ptoDays = state.ptoDays,
                    onDeleteDay = state.onDeleteDay,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
            ViewMode.CALENDAR -> {
                PTOCalendarView(
                    ptoDays = state.ptoDays,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
        }
    }
}

@Composable
private fun PTOListView(
    ptoDays: List<PTODay>,
    onDeleteDay: (PTODay) -> Unit,
    modifier: Modifier = Modifier
) {
    if (ptoDays.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No PTO days recorded yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ptoDays) { day ->
                PTODayItem(day = day, onDelete = { onDeleteDay(day) })
            }
        }
    }
}

@Composable
private fun PTODayItem(day: PTODay, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatDate(day.date),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = getDayOfWeek(day.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun PTOCalendarView(
    ptoDays: List<PTODay>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📅",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Calendar view coming soon!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${ptoDays.size} PTO days recorded",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    return "${months[date.monthNumber - 1]} ${date.dayOfMonth}, ${date.year}"
}

private fun getDayOfWeek(date: LocalDate): String {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    return days[date.dayOfWeek.ordinal]
}
