package com.ptotracker.domain.models

data class PTOSummary(
    val daysTaken: Int,
    val targetDays: Int,
    val status: PTOStatus
) {
    companion object {
        fun calculate(daysTaken: Int, targetDays: Int): PTOSummary {
            val percentage = if (targetDays > 0) {
                (daysTaken.toFloat() / targetDays.toFloat())
            } else {
                0f
            }

            val status = when {
                percentage >= 0.75f -> PTOStatus.GOOD
                percentage >= 0.40f -> PTOStatus.WARNING
                else -> PTOStatus.CRITICAL
            }

            return PTOSummary(
                daysTaken = daysTaken,
                targetDays = targetDays,
                status = status
            )
        }
    }
}
