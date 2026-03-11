package com.mina.weatherapp.nav

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mina.weatherapp.data.db.WeatherDatabase
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.data.weather.datasource.local.FavoritesLocalDataSource
import com.mina.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.mina.weatherapp.presentation.favourites.favouritesdetails.FavoriteDetailsScreen
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.AddFavoriteViewModel
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.AddFavoriteViewModelFactory
import com.mina.weatherapp.presentation.favourites.favouritesdetails.FavoriteDetailsViewModel
import com.mina.weatherapp.presentation.favourites.favouritesdetails.FavoriteDetailsViewModelFactory
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.AddLocationScreen
import com.mina.weatherapp.presentation.favourites.favouriteslocations.FavoritesScreen
import com.mina.weatherapp.presentation.favourites.favouriteslocations.FavoritesViewModel
import com.mina.weatherapp.presentation.favourites.favouriteslocations.FavoritesViewModelFactory
import com.mina.weatherapp.presentation.home.HomeUiState
import com.mina.weatherapp.screens.*

@Composable
fun WeatherAppRoot(uiState: HomeUiState, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    val repository = remember {
        val database = WeatherDatabase.getInstance(context)
        val localDataSource = FavoritesLocalDataSource(database.favoriteLocationDao())
        val remoteDataSource = WeatherRemoteDataSource()
        WeatherRepository(remoteDataSource, localDataSource)
    }

    val favoritesFactory = remember { FavoritesViewModelFactory(repository) }
    val addFavoriteFactory = remember { AddFavoriteViewModelFactory(repository) }
    val favoriteDetailsFactory = remember { FavoriteDetailsViewModelFactory(repository) }

    val favoritesViewModel: FavoritesViewModel = viewModel(factory = favoritesFactory)
    val addFavoriteViewModel: AddFavoriteViewModel = viewModel(factory = addFavoriteFactory)
    val favoriteDetailsViewModel: FavoriteDetailsViewModel = viewModel(factory = favoriteDetailsFactory)

    val favoritesUiState by favoritesViewModel.uiState.collectAsState()
    val addFavoriteUiState by addFavoriteViewModel.uiState.collectAsState()
    val favoriteDetailsUiState by favoriteDetailsViewModel.uiState.collectAsState()

    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination

    val isHome = currentDestination?.hierarchy?.any { it.route == Screens.Home::class.qualifiedName } == true
    val isFavorites = currentDestination?.hierarchy?.any { it.route == Screens.Favorites::class.qualifiedName } == true
    val isAlerts = currentDestination?.hierarchy?.any { it.route == Screens.Alerts::class.qualifiedName } == true
    val isSettings = currentDestination?.hierarchy?.any { it.route == Screens.Settings::class.qualifiedName } == true

    val showBottomBar = isHome || isFavorites || isAlerts || isSettings

    BackHandler(enabled = showBottomBar && !isHome) {
        navController.navigate(Screens.Home) {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateTab(destination: Screens) {
        navController.navigate(destination) {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
            launchSingleTop = true
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = isHome,
                        onClick = { if (!isHome) navigateTab(Screens.Home) },
                        icon = { Icon(tabHome.icon, contentDescription = tabHome.label) },
                        label = { Text(tabHome.label) }
                    )
                    NavigationBarItem(
                        selected = isFavorites,
                        onClick = { if (!isFavorites) navigateTab(Screens.Favorites) },
                        icon = { Icon(tabFavorites.icon, contentDescription = tabFavorites.label) },
                        label = { Text(tabFavorites.label) }
                    )
                    NavigationBarItem(
                        selected = isAlerts,
                        onClick = { if (!isAlerts) navigateTab(Screens.Alerts) },
                        icon = { Icon(tabAlerts.icon, contentDescription = tabAlerts.label) },
                        label = { Text(tabAlerts.label) }
                    )
                    NavigationBarItem(
                        selected = isSettings,
                        onClick = { if (!isSettings) navigateTab(Screens.Settings) },
                        icon = { Icon(tabSettings.icon, contentDescription = tabSettings.label) },
                        label = { Text(tabSettings.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable<Screens.Home> {
                WeatherForecastScreen(uiState)
            }

            composable<Screens.Favorites> {
                FavoritesScreen(
                    uiState = favoritesUiState,
                    onAddLocation = {
                        addFavoriteViewModel.resetScreen()
                        navController.navigate(Screens.AddLocation)
                    },
                    onItemClick = { lat, lon ->
                        favoriteDetailsViewModel.loadFavoriteWeather(lat, lon)
                        navController.navigate(Screens.FavoriteDetails(lat, lon))
                    },
                    onDeleteClick = { favoritesViewModel.deleteFavorite(it) }
                )
            }

            composable<Screens.AddLocation> {
                AddLocationScreen(
                    uiState = addFavoriteUiState,
                    selectedLocation = addFavoriteViewModel.selectedLocation.collectAsState().value,
                    onBack = { navController.popBackStack() },
                    onLocationSelected = { lat, lon ->
                        addFavoriteViewModel.setSelectedLocation(lat, lon)
                    },
                    onSave = { cityName ->
                        addFavoriteViewModel.saveFavorite(cityName)
                    }
                )
            }

            composable<Screens.FavoriteDetails> {
                FavoriteDetailsScreen(uiState = favoriteDetailsUiState)
            }

            composable<Screens.Alerts> {
                AlertsScreen(
                    onAddAlert = { navController.navigate(Screens.AddAlert) }
                )
            }

            composable<Screens.Settings> {
                SettingsScreen(
                    onBack = { navController.navigate(Screens.Home) { launchSingleTop = true } }
                )
            }

            composable<Screens.AddAlert> {
                AddAlertScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
        }
    }
}
