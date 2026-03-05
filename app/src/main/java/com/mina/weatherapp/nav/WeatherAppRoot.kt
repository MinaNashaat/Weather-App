package com.mina.weatherapp.nav

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mina.weatherapp.screens.AddAlertScreen
import com.mina.weatherapp.screens.AddLocationScreen
import com.mina.weatherapp.screens.AlertsScreen
import com.mina.weatherapp.screens.FavoritesScreen
import com.mina.weatherapp.screens.Screens
import com.mina.weatherapp.screens.SettingsScreen
import com.mina.weatherapp.screens.WeatherForecastScreen

@Composable
fun WeatherAppRoot() {
    val navController = rememberNavController()
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
            modifier = Modifier.padding(padding)
        ) {
            composable<Screens.Home> {
                WeatherForecastScreen()
            }

            composable<Screens.Favorites> {
                FavoritesScreen(
                    onBack = { navController.navigate(Screens.Home) { launchSingleTop = true } },
                    onAddLocation = { navController.navigate(Screens.AddLocation) { launchSingleTop = true } }
                )
            }

            composable<Screens.Alerts> {
                AlertsScreen(
                    onAddAlert = { navController.navigate(Screens.AddAlert) { launchSingleTop = true } }
                )
            }

            composable<Screens.Settings> {
                SettingsScreen(onBack = { navController.navigate(Screens.Home) { launchSingleTop = true } })
            }

            composable<Screens.AddLocation> {
                AddLocationScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
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