package com.mina.weatherapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    onAddAlert: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alerts") },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = null) }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAlert) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Alerts UI here")
        }
    }
}