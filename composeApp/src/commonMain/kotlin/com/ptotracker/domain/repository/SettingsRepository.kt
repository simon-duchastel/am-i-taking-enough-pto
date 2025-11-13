package com.ptotracker.domain.repository

import com.ptotracker.domain.models.PTOSettings
import com.ptotracker.domain.models.YearMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<PTOSettings>
    suspend fun updateTargetPTODays(days: Int)
    suspend fun updateYearMode(mode: YearMode)
}
