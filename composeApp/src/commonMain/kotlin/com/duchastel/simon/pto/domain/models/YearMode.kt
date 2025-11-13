package com.duchastel.simon.pto.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class YearMode {
    CALENDAR_YEAR,
    ROLLING_365_DAYS
}
