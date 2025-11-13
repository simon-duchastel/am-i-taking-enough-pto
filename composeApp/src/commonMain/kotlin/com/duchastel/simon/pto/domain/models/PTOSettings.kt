package com.duchastel.simon.pto.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PTOSettings(
    val targetPTODays: Int = 15,
    val yearMode: YearMode = YearMode.CALENDAR_YEAR
)
