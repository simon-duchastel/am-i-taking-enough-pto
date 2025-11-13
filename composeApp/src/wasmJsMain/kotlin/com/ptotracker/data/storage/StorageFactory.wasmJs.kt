package com.ptotracker.data.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.localStorage

actual object StorageFactory {
    actual fun createSettings(): Settings {
        return StorageSettings(localStorage)
    }
}
