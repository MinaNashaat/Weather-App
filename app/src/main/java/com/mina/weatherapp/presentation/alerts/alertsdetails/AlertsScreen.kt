package com.mina.weatherapp.presentation.alerts.alertsdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    uiState: AlertsUiState,
    onAddAlert: () -> Unit,
    onDelete: (AlertEntity) -> Unit,
    onToggle: (AlertEntity, Boolean) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Alerts") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAlert) {
                Icon(Icons.Default.Add, contentDescription = "Add alert")
            }
        }
    ) { padding ->

        when (uiState) {
            is AlertsUiState.Loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is AlertsUiState.Error -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(uiState.message) }

            is AlertsUiState.Success -> {
                if (uiState.data.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) { Text("No alerts yet") }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.data) { alert ->
                            AlertItem(
                                alert = alert,
                                onDelete = { onDelete(alert) },
                                onToggle = { onToggle(alert, it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertItem(
    alert: AlertEntity,
    onDelete: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(alert.title, style = MaterialTheme.typography.titleMedium)
                Text("Condition: ${alert.conditionType}")
                Text("Action: ${alert.actionType}")
            }

            Switch(
                checked = alert.enabled,
                onCheckedChange = onToggle
            )

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}