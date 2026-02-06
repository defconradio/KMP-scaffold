package com.example.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ScreenC(onNavigate: (com.example.shared.RootScreen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is Screen C")
        Button(onClick = { onNavigate(com.example.shared.RootScreen.Home) }) {
            Text("Go back to Screen A")
        }
        Button(onClick = { onNavigate(com.example.shared.RootScreen.Trade) }) {
            Text("Back")
        }
    }
}
