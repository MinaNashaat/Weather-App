package com.mina.weatherapp.presentation.favourites.addfavouritelocation

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.SelectedLocation
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    uiState: AddFavoriteUiState,
    selectedLocation: SelectedLocation?,
    onBack: () -> Unit,
    onLocationSelected: (Double, Double) -> Unit,
    onSave: (String) -> Unit
) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }

    LaunchedEffect(selectedLocation) {
        if (selectedLocation != null) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val result = geocoder.getFromLocation(
                    selectedLocation.latitude,
                    selectedLocation.longitude,
                    1
                )
                val address = result?.firstOrNull()
                cityName = address?.locality
                    ?: address?.subAdminArea
                            ?: address?.adminArea
                            ?: cityName
            } catch (_: Exception) {
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick Favorite Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
                    selectedLat = selectedLocation?.latitude,
                    selectedLon = selectedLocation?.longitude,
                    onMapClick = onLocationSelected
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("City name") }
                )

                selectedLocation?.let {
                    Text("Lat: ${it.latitude}")
                    Text("Lon: ${it.longitude}")
                }

                when (uiState) {
                    is AddFavoriteUiState.Error -> {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is AddFavoriteUiState.Saving -> {
                        Log.d("minanashaat" , "AddLocationScreen : Saving")
                        CircularProgressIndicator()
                    }
                    is AddFavoriteUiState.Success -> {
                        Log.d("minanashaat" , "AddLocationScreen : Success")
                        LaunchedEffect(Unit) {
                            onBack()
                        }
                    }
                    else -> Unit
                }

                Button(
                    onClick = { onSave(cityName) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedLocation != null
                ) {
                    Text("Save to Favorites")
                }
            }
        }
    }
}