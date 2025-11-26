package com.duchastel.simon.pto.data.repository

import com.duchastel.simon.pto.domain.models.PTOSettings
import com.duchastel.simon.pto.domain.models.YearMode
import com.duchastel.simon.pto.domain.repository.SettingsRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryImpl(
    private val settings: Settings
) : SettingsRepository {

    private val _settingsFlow = MutableStateFlow(loadSettings())

    private companion object {
        const val KEY_TARGET_PTO_DAYS = "target_pto_days"
        const val KEY_YEAR_MODE = "year_mode"
        const val DEFAULT_TARGET_DAYS = 15
    }

    private fun loadSettings(): PTOSettings {
        val targetDays = settings.getInt(KEY_TARGET_PTO_DAYS, DEFAULT_TARGET_DAYS)
        val yearModeString = settings.getString(KEY_YEAR_MODE, YearMode.CALENDAR_YEAR.name)
        val yearMode = YearMode.valueOf(yearModeString)

        return PTOSettings(
            targetPTODays = targetDays,
            yearMode = yearMode
        )
    }

    override fun getSettings(): Flow<PTOSettings> = _settingsFlow.asStateFlow()

    override suspend fun updateTargetPTODays(days: Int) {
        settings[KEY_TARGET_PTO_DAYS] = days
        _settingsFlow.update { it.copy(targetPTODays = days) }
    }

    override suspend fun updateYearMode(mode: YearMode) {
        settings[KEY_YEAR_MODE] = mode.name
        _settingsFlow.update { it.copy(yearMode = mode) }
    }
}
