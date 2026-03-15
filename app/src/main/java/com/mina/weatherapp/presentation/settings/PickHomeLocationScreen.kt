package com.mina.weatherapp.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.OsmMapPicker
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.SelectedLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickHomeLocationScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onSavedGoHome: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is SettingsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SettingsUiState.Success -> {
            val currentSettings = (state as SettingsUiState.Success).data
            var selected by remember {
                mutableStateOf<SelectedLocation?>(
                    if (currentSettings.homeLat != null && currentSettings.homeLon != null)
                        SelectedLocation(currentSettings.homeLat, currentSettings.homeLon)
                    else
                        null
                )
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Pick Home Location") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        OsmMapPicker(
                            selectedLat = selected?.latitude,
                            selectedLon = selected?.longitude,
                            onMapClick = { lat, lon -> selected = SelectedLocation(lat, lon) }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        selected?.let {
                            Text("Lat: ${it.latitude}")
                            Text("Lon: ${it.longitude}")
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selected != null,
                            onClick = {
                                val loc = selected ?: return@Button
                                viewModel.updateHomeLocation(loc.latitude, loc.longitude)
                                viewModel.updateLocationSource(LocationSource.MAP)
                                onSavedGoHome()
                            }
                        ) {
                            Text("Save Home Location")
                        }
                    }
                }
            }
        }

        is SettingsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error")
            }
        }
    }
}