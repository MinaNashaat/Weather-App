package com.mina.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.nav.WeatherAppRoot
import com.mina.weatherapp.presentation.home.HomeViewModel
import com.mina.weatherapp.presentation.home.WeatherViewModelFactory

const val LOCATION_PERMISSION = 27
class MainActivity : ComponentActivity() {
    private lateinit var locationState: MutableState<Location>

    private val viewModel: HomeViewModel by viewModels {
        val container = (application as WeatherApp).appContainer
        WeatherViewModelFactory(
            weatherRepository = container.weatherRepository,
            settingsRepository = container.settingsRepository
        )
    }
    private val requestNotifPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState = viewModel.uiState.collectAsState().value

            MaterialTheme(colorScheme = lightColorScheme()) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    locationState = rememberSaveable{ mutableStateOf(Location("")) }
                    WeatherAppRoot(uiState, viewModel, Modifier.fillMaxSize())
//                }
            }
        }
    }
    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotifPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }


        val source = (application as WeatherApp)
            .appContainer
            .settingsRepository
            .settings
            .value
            .locationSource

        if (source == LocationSource.MAP) {
            viewModel.refresh()
            return
        }

        if(checkPermission()){
            if(isLocationEnabled()){
                getCurrentLocationOnce()
            }
            else{
                enableLocationService()
            }
        }
        else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("minanashaat" , "i am in onRequestPermissionsResult without deviceId")
        if(requestCode == LOCATION_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(isLocationEnabled()){
                    getCurrentLocationOnce()
                }
                else{
                    enableLocationService()
                }
            }
        }
    }

    fun checkPermission() = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun enableLocationService(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationOnce() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                viewModel.updateGpsLocation(location.latitude, location.longitude)
            }
        }
    }
}
