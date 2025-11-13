package com.duchastel.simon.pto.data.storage

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual object StorageFactory {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    actual fun createSettings(): Settings {
        val sharedPreferences = appContext.getSharedPreferences(
            "pto_tracker_prefs",
            Context.MODE_PRIVATE
        )
        return SharedPreferencesSettings(sharedPreferences)
    }
}
