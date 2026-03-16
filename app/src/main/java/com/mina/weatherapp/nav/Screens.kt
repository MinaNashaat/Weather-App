package com.mina.weatherapp.nav

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Home : Screens()

    @Serializable
    data object Favorites : Screens()

    @Serializable
    data object Alerts : Screens()

    @Serializable
    data object Settings : Screens()

    @Serializable
    data object AddLocation : Screens()

    @Serializable
    data object AddAlert : Screens()

    @Serializable
    data class FavoriteDetails(val latitude: Double, val longitude: Double) : Screens()

    @Serializable
    data object PickHomeLocation : Screens()
}