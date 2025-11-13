package com.duchastel.simon.pto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.duchastel.simon.pto.data.storage.StorageFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StorageFactory.init(this)

        setContent {
            App()
        }
    }
}
