package com.mina.weatherapp.presentation.alerts.addalerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mina.weatherapp.data.alerts.local.entity.AlertActionType
import com.mina.weatherapp.data.alerts.local.entity.AlertConditionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertScreen(
    viewModel: AddAlertViewModel,
    homeLat: Double,
    homeLon: Double,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("Weather Alert") }
    var condition by remember { mutableStateOf(AlertConditionType.RAIN) }
    var action by remember { mutableStateOf(AlertActionType.NOTIFICATION) }
    var thresholdText by remember { mutableStateOf("") }

    var startMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var endMillis by remember { mutableStateOf(System.currentTimeMillis() + 60 * 60 * 1000) }

    val fmt = remember { SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()) }

    LaunchedEffect(state) {
        if (state is AddAlertUiState.Saved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Alert") },
                navigationIcon = { IconButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") }
            )

            Text("Location: $homeLat, $homeLon")


            Text("Duration (active window)")

            OutlinedButton(
                onClick = {
                    viewModel.showDateTimePicker(context, startMillis) { picked ->
                        startMillis = picked
                        if (endMillis <= startMillis) {
                            endMillis = startMillis + 60 * 60 * 1000
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start: ${fmt.format(Date(startMillis))}")
            }

            OutlinedButton(
                onClick = {
                    viewModel.showDateTimePicker(context, endMillis) { picked ->
                        endMillis = picked
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("End:   ${fmt.format(Date(endMillis))}")
            }


            Text("Condition")
            ConditionSelector(condition) { condition = it }

            if (condition == AlertConditionType.WIND ||
                condition == AlertConditionType.TEMP_HIGH ||
                condition == AlertConditionType.TEMP_LOW
            ) {
                OutlinedTextField(
                    value = thresholdText,
                    onValueChange = { thresholdText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Threshold (number)") }
                )
            }


            Text("Type")
            Row {
                RadioButton(
                    selected = action == AlertActionType.NOTIFICATION,
                    onClick = { action = AlertActionType.NOTIFICATION }
                )
                Text("Notification", Modifier.padding(top = 12.dp))
            }
            Row {
                RadioButton(
                    selected = action == AlertActionType.ALARM,
                    onClick = { action = AlertActionType.ALARM }
                )
                Text("Alarm sound", Modifier.padding(top = 12.dp))
            }

            when (state) {
                is AddAlertUiState.Error ->
                    Text((state as AddAlertUiState.Error).message, color = MaterialTheme.colorScheme.error)
                is AddAlertUiState.Saving -> CircularProgressIndicator()
                else -> Unit
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val threshold = thresholdText.toDoubleOrNull()
                    viewModel.save(
                        title = title,
                        lat = homeLat,
                        lon = homeLon,
                        start = startMillis,
                        end = endMillis,
                        condition = condition,
                        threshold = threshold,
                        action = action
                    )
                }
            ) { Text("Save") }
        }
    }
}

@Composable
private fun ConditionSelector(
    selected: AlertConditionType,
    onSelect: (AlertConditionType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AlertConditionType.values().forEach { c ->
            Row {
                RadioButton(selected = selected == c, onClick = { onSelect(c) })
                Text(c.name, Modifier.padding(top = 12.dp))
            }
        }
    }
}

