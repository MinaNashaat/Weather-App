package com.mina.weatherapp.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomTabUi(val label: String, val icon: ImageVector)

val tabHome = BottomTabUi("Home", Icons.Filled.Home)
val tabFavorites = BottomTabUi("Favorites", Icons.Filled.Favorite)
val tabAlerts = BottomTabUi("Alerts", Icons.Filled.Notifications)
val tabSettings = BottomTabUi("Settings", Icons.Filled.Settings)