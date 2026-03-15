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
import com.mina.weatherapp.WeatherApp
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
import com.mina.weatherapp.presentation.home.HomeViewModel
import com.mina.weatherapp.presentation.settings.PickHomeLocationScreen
import com.mina.weatherapp.presentation.settings.SettingsScreen
import com.mina.weatherapp.presentation.settings.SettingsViewModel
import com.mina.weatherapp.presentation.settings.SettingsViewModelFactory
import com.mina.weatherapp.screens.*

@Composable
fun WeatherAppRoot(
    uiState: HomeUiState,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as WeatherApp
    val settingsRepository = app.appContainer.settingsRepository
    val repository = app.appContainer.weatherRepository

    val favoritesFactory = remember { FavoritesViewModelFactory(repository) }
    val addFavoriteFactory = remember { AddFavoriteViewModelFactory(repository) }
    val favoriteDetailsFactory = remember { FavoriteDetailsViewModelFactory(repository) }
    val settingsFactory = remember { SettingsViewModelFactory(settingsRepository) }

    val favoritesViewModel: FavoritesViewModel = viewModel(factory = favoritesFactory)
    val addFavoriteViewModel: AddFavoriteViewModel = viewModel(factory = addFavoriteFactory)
    val favoriteDetailsViewModel: FavoriteDetailsViewModel = viewModel(factory = favoriteDetailsFactory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsFactory)


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
                    viewModel = settingsViewModel,
                    onBack = { navController.navigate(Screens.Home) { launchSingleTop = true } },
                    onPickHomeLocation = { navController.navigate(Screens.PickHomeLocation) }
                )
            }

            composable<Screens.AddAlert> {
                AddAlertScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable<Screens.PickHomeLocation> {
                PickHomeLocationScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() },
                    onSavedGoHome = {
                        navController.navigate(Screens.Home) {
                            launchSingleTop = true
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                        }
                    }
                )
            }

        }
    }
}
