package com.ptotracker.data.storage

import com.russhwolf.settings.Settings

expect object StorageFactory {
    fun createSettings(): Settings
}
