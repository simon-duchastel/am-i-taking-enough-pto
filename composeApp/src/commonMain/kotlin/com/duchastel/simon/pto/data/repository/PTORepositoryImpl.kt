package com.duchastel.simon.pto.data.repository

import com.duchastel.simon.pto.domain.models.PTODay
import com.duchastel.simon.pto.domain.repository.PTORepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PTORepositoryImpl(
    private val settings: Settings
) : PTORepository {

    private val _ptoDaysFlow = MutableStateFlow(loadPTODays())

    private companion object {
        const val KEY_PTO_DAYS = "pto_days"
    }

    private fun loadPTODays(): List<PTODay> {
        val json = settings.getString(KEY_PTO_DAYS, "[]")
        return try {
            Json.decodeFromString<List<PTODay>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun savePTODays(days: List<PTODay>) {
        val json = Json.encodeToString(days)
        settings[KEY_PTO_DAYS] = json
        _ptoDaysFlow.value = days
    }

    override fun getAllPTODays(): Flow<List<PTODay>> = _ptoDaysFlow.asStateFlow()

    override suspend fun addPTODay(date: LocalDate) {
        val currentDays = _ptoDaysFlow.value.toMutableList()
        val newDay = PTODay(date)
        if (!currentDays.contains(newDay)) {
            currentDays.add(newDay)
            currentDays.sortBy { it.date }
            savePTODays(currentDays)
        }
    }

    override suspend fun addPTODays(dates: List<LocalDate>) {
        val currentDays = _ptoDaysFlow.value.toMutableList()
        val newDays = dates.map { PTODay(it) }.filter { !currentDays.contains(it) }
        if (newDays.isNotEmpty()) {
            currentDays.addAll(newDays)
            currentDays.sortBy { it.date }
            savePTODays(currentDays)
        }
    }

    override suspend fun removePTODay(date: LocalDate) {
        val currentDays = _ptoDaysFlow.value.toMutableList()
        currentDays.removeAll { it.date == date }
        savePTODays(currentDays)
    }

    override suspend fun getPTODaysInRange(start: LocalDate, end: LocalDate): List<PTODay> {
        return _ptoDaysFlow.value.filter { it.date in start..end }
    }
}
