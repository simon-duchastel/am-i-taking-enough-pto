package com.ptotracker.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PTODay(
    val date: LocalDate
)
