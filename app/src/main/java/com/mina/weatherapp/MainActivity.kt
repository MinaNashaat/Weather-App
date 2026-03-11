package com.mina.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mina.weatherapp.data.db.WeatherDatabase
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.data.weather.datasource.local.FavoritesLocalDataSource
import com.mina.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.mina.weatherapp.nav.WeatherAppRoot
import com.mina.weatherapp.presentation.home.HomeViewModel
import com.mina.weatherapp.presentation.home.WeatherViewModelFactory
import com.mina.weatherapp.ui.theme.WeatherAppTheme
import com.mina.weatherapp.utils.Constants

const val LOCATION_PERMISSION = 27
class MainActivity : ComponentActivity() {
    private lateinit var locationState: MutableState<Location>
    private val viewModel: HomeViewModel by viewModels {
        val database = WeatherDatabase.getInstance(application)
        val favoriteLocationDao = database.favoriteLocationDao()
        val localDataSource = FavoritesLocalDataSource(favoriteLocationDao)
        val remoteDataSource = WeatherRemoteDataSource()
        val repository = WeatherRepository(remoteDataSource, localDataSource)
        WeatherViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState = viewModel.uiState.collectAsState().value

            MaterialTheme(colorScheme = lightColorScheme()) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    locationState = rememberSaveable{ mutableStateOf(Location("")) }
                    WeatherAppRoot(uiState, Modifier.fillMaxSize())
//                }
            }
        }
    }
    override fun onStart() {
        super.onStart()

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
                viewModel.getHomeWeather(
                    lat = location.latitude,
                    lon = location.longitude,
                    appid = Constants.API_KEY
                )
            }
        }
    }
}