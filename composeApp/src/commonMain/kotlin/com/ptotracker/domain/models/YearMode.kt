package com.ptotracker.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class YearMode {
    CALENDAR_YEAR,
    ROLLING_365_DAYS
}
