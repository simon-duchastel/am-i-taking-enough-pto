package com.duchastel.simon.pto.domain.repository

import com.duchastel.simon.pto.domain.models.PTODay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PTORepository {
    fun getAllPTODays(): Flow<List<PTODay>>
    suspend fun addPTODay(date: LocalDate)
    suspend fun addPTODays(dates: List<LocalDate>)
    suspend fun removePTODay(date: LocalDate)
    suspend fun getPTODaysInRange(start: LocalDate, end: LocalDate): List<PTODay>
}
