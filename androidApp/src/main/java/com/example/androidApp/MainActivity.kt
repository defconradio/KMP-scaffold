package com.example.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.shared.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure system bars are handled for edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            App()
        }
    }
}
