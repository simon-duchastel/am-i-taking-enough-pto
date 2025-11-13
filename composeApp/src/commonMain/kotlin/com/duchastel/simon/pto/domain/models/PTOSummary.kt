package com.duchastel.simon.pto.domain.models

data class PTOSummary(
    val daysTaken: Int,
    val targetDays: Int,
    val status: PTOStatus,
    val yearProgress: Float
) {
    companion object {
        fun calculate(daysTaken: Int, targetDays: Int, yearProgress: Float): PTOSummary {
            val actualProgress = if (targetDays > 0) {
                daysTaken.toFloat() / targetDays.toFloat()
            } else {
                0f
            }

            val expectedProgress = yearProgress.coerceAtLeast(0.05f)

            val ratio = if (expectedProgress > 0f) {
                actualProgress / expectedProgress
            } else {
                0f
            }

            val status = when {
                ratio >= 1.0f -> PTOStatus.GOOD
                ratio >= 0.7f -> PTOStatus.WARNING
                else -> PTOStatus.CRITICAL
            }

            return PTOSummary(
                daysTaken = daysTaken,
                targetDays = targetDays,
                status = status,
                yearProgress = yearProgress
            )
        }
    }
}
