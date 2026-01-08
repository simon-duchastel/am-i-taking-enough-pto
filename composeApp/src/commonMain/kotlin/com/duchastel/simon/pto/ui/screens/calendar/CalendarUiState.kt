package com.duchastel.simon.pto.ui.screens.calendar

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.datetime.LocalDate

sealed interface CalendarUiState : CircuitUiState {
    data object Loading : CalendarUiState

    data class Loaded(
        val currentMonth: Int,
        val currentYear: Int,
        val ptoDays: Set<LocalDate>,
        val calendarDays: List<CalendarDay>,
        val onPreviousMonth: () -> Unit,
        val onNextMonth: () -> Unit,
        val onDayClick: (LocalDate) -> Unit,
        val onBack: () -> Unit
    ) : CalendarUiState
}

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isPTO: Boolean,
    val isToday: Boolean
)
