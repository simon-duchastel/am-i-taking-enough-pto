package com.duchastel.simon.pto.ui.screens.calendar

import androidx.compose.runtime.*
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.duchastel.simon.pto.ui.navigation.ViewPTOScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.datetime.*

class CalendarPresenter(
    private val navigator: Navigator,
    private val ptoRepository: PTORepository
) : Presenter<CalendarUiState> {

    @Composable
    override fun present(): CalendarUiState {
        val ptoDays by ptoRepository.getAllPTODays().collectAsState(initial = null)

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        var currentMonth by remember { mutableStateOf(now.monthNumber) }
        var currentYear by remember { mutableStateOf(now.year) }

        if (ptoDays == null) {
            return CalendarUiState.Loading
        }

        val ptoDates = ptoDays!!.map { it.date }.toSet()
        val calendarDays = generateCalendarDays(currentYear, currentMonth, ptoDates, now.date)

        return CalendarUiState.Loaded(
            currentMonth = currentMonth,
            currentYear = currentYear,
            ptoDays = ptoDates,
            calendarDays = calendarDays,
            onPreviousMonth = {
                if (currentMonth == 1) {
                    currentMonth = 12
                    currentYear -= 1
                } else {
                    currentMonth -= 1
                }
            },
            onNextMonth = {
                if (currentMonth == 12) {
                    currentMonth = 1
                    currentYear += 1
                } else {
                    currentMonth += 1
                }
            },
            onDayClick = { date ->
                // Navigate to ViewPTO screen when a PTO day is clicked
                if (date in ptoDates) {
                    navigator.goTo(ViewPTOScreen)
                }
            },
            onBack = {
                navigator.pop()
            }
        )
    }

    private fun generateCalendarDays(
        year: Int,
        month: Int,
        ptoDates: Set<LocalDate>,
        today: LocalDate
    ): List<CalendarDay> {
        val firstDayOfMonth = LocalDate(year, month, 1)

        // Calculate last day of month by going to next month and subtracting a day
        val nextMonthDate = if (month == 12) {
            LocalDate(year + 1, 1, 1)
        } else {
            LocalDate(year, month + 1, 1)
        }
        val lastDayOfMonthNumber = (nextMonthDate.toEpochDays() - firstDayOfMonth.toEpochDays()).toInt()
        val lastDayOfMonth = LocalDate(year, month, lastDayOfMonthNumber)

        // Get the day of week for the first day (1 = Monday, 7 = Sunday)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber

        // Calculate how many days from previous month to show
        val daysFromPrevMonth = if (firstDayOfWeek == 7) 0 else firstDayOfWeek

        val days = mutableListOf<CalendarDay>()

        // Add days from previous month
        if (daysFromPrevMonth > 0) {
            val prevMonth = if (month == 1) 12 else month - 1
            val prevYear = if (month == 1) year - 1 else year

            // Calculate last day of previous month
            val prevMonthFirst = LocalDate(prevYear, prevMonth, 1)
            val currentMonthFirst = LocalDate(year, month, 1)
            val lastDayOfPrevMonth = (currentMonthFirst.toEpochDays() - prevMonthFirst.toEpochDays()).toInt()

            for (day in (lastDayOfPrevMonth - daysFromPrevMonth + 1)..lastDayOfPrevMonth) {
                val date = LocalDate(prevYear, prevMonth, day)
                days.add(
                    CalendarDay(
                        date = date,
                        isCurrentMonth = false,
                        isPTO = date in ptoDates,
                        isToday = date == today
                    )
                )
            }
        }

        // Add days from current month
        for (day in 1..lastDayOfMonthNumber) {
            val date = LocalDate(year, month, day)
            days.add(
                CalendarDay(
                    date = date,
                    isCurrentMonth = true,
                    isPTO = date in ptoDates,
                    isToday = date == today
                )
            )
        }

        // Add days from next month to complete the grid (make it 6 weeks / 42 days)
        val remainingDays = 42 - days.size
        val nextMonth = if (month == 12) 1 else month + 1
        val nextYear = if (month == 12) year + 1 else year

        for (day in 1..remainingDays) {
            val date = LocalDate(nextYear, nextMonth, day)
            days.add(
                CalendarDay(
                    date = date,
                    isCurrentMonth = false,
                    isPTO = date in ptoDates,
                    isToday = date == today
                )
            )
        }

        return days
    }
}
