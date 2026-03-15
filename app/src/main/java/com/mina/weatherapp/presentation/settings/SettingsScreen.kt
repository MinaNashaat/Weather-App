package com.mina.weatherapp.presentation.settings

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.WindSpeedUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onPickHomeLocation: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
//    val currentSettings = (state as? SettingsUiState.Success)?.data

    fun applyChange(change: () -> Unit) {
        change()
        onBack()
    }

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
            val context = LocalContext.current
            val fusedLocationClient = remember {
                LocationServices.getFusedLocationProviderClient(context)
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Settings") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text("Location")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.locationSource.name.lowercase() == "gps",
                            onClick = {
                                //here
                                ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

                                fusedLocationProviderClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    null
                                ).addOnSuccessListener { location ->
                                    if (location != null) {
                                        viewModel.updateHomeLocation(location.latitude, location.longitude)
                                        viewModel.updateLocationSource(LocationSource.GPS)
                                        onBack()
                                    }
                                }

                            }
                        )
                        Text("GPS")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.locationSource.name.lowercase() == "map",
                            onClick = {
                                viewModel.updateLocationSource(LocationSource.MAP)

                                val hasHome = (currentSettings.homeLat != null && currentSettings.homeLon != null)
                                if (!hasHome)
                                    onPickHomeLocation()
                                else
                                    onBack()
                            }
                        )
                        Text("Map")
                    }

                    if (currentSettings.locationSource.name.lowercase() == "map") {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onPickHomeLocation,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pick Home Location on Map")
                        }
                    }

                    Text("Temperature Units")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.units == "standard",
                            onClick = {
                                applyChange {
                                    viewModel.updateUnits("standard")
                                }
                            }
                        )
                        Text("Kelvin")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.units == "metric",
                            onClick = {
                                applyChange {
                                    viewModel.updateUnits("metric")
                                }
                            }
                        )
                        Text("Celsius")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.units == "imperial",
                            onClick = {
                                applyChange {
                                    viewModel.updateUnits("imperial")
                                }
                            }
                        )
                        Text("Fahrenheit")
                    }

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    Text("Wind Speed Units")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.windSpeedUnit == WindSpeedUnit.MPS,
                            onClick = {
                                applyChange {
                                    viewModel.updateWindSpeedUnit(WindSpeedUnit.MPS)
                                }
                            }
                        )
                        Text("Meter/Sec (mps)")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.windSpeedUnit == WindSpeedUnit.MPH,
                            onClick = {
                                applyChange {
                                    viewModel.updateWindSpeedUnit(WindSpeedUnit.MPH)
                                }
                            }
                        )
                        Text("Miles/Hour (mph)")
                    }

                    Spacer(modifier = Modifier.padding(top = 12.dp))

                    Text("Language")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.language == "en",
                            onClick = { applyChange { viewModel.updateLanguage("en") } }
                        )
                        Text("English")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentSettings.language == "ar",
                            onClick = { applyChange { viewModel.updateLanguage("ar") } }
                        )
                        Text("Arabic")
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
