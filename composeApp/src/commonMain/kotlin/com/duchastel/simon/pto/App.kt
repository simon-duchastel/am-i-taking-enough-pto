package com.duchastel.simon.pto

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.duchastel.simon.pto.data.repository.PTORepositoryImpl
import com.duchastel.simon.pto.data.repository.SettingsRepositoryImpl
import com.duchastel.simon.pto.data.storage.StorageFactory
import com.duchastel.simon.pto.ui.PTOApp
import com.duchastel.simon.pto.ui.PTOCircuitFactory

@Composable
fun App() {
    MaterialTheme {
        val settings = StorageFactory.createSettings()
        val ptoRepository = PTORepositoryImpl(settings)
        val settingsRepository = SettingsRepositoryImpl(settings)

        val circuit = PTOCircuitFactory(ptoRepository, settingsRepository).create()

        PTOApp(circuit)
    }
}
