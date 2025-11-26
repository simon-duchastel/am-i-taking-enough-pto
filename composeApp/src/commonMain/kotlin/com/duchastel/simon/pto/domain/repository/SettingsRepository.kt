package com.duchastel.simon.pto.domain.repository

import com.duchastel.simon.pto.domain.models.PTOSettings
import com.duchastel.simon.pto.domain.models.YearMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<PTOSettings>
    suspend fun updateTargetPTODays(days: Int)
    suspend fun updateYearMode(mode: YearMode)
}
