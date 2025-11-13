package com.duchastel.simon.pto.data.storage

import com.russhwolf.settings.Settings

expect object StorageFactory {
    fun createSettings(): Settings
}
