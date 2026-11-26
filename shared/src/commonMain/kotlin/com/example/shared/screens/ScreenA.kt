package com.example.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.resources.Res
import com.example.shared.resources.shared_greeting
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScreenA(
    onNavigateToTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.shared_greeting))
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onNavigateToTrade) {
            Text("Go to Screen B")
        }
    }
}
