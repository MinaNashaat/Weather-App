package com.mina.weatherapp.presentation.favourites.addfavouritelocation

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

@Composable
fun OsmMapPicker(
    selectedLat: Double?,
    selectedLon: Double?,
    onMapClick: (Double, Double) -> Unit
) {
    val markerHolder = remember { arrayOfNulls<Marker>(1) }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(6.0)
                controller.setCenter(GeoPoint(30.0444, 31.2357)) // Cairo default

                overlays.add(object : Overlay() {
                    override fun onSingleTapConfirmed(
                        e: MotionEvent?,
                        mapView: MapView?
                    ): Boolean {
                        if (e == null || mapView == null) return false

                        val projection = mapView.projection
                        val geoPoint = projection.fromPixels(
                            e.x.toInt(),
                            e.y.toInt()
                        ) as GeoPoint

                        onMapClick(geoPoint.latitude, geoPoint.longitude)

                        markerHolder[0]?.let { mapView.overlays.remove(it) }

                        val marker = Marker(mapView).apply {
                            position = geoPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Selected location"
                        }

                        markerHolder[0] = marker
                        mapView.overlays.add(marker)
                        mapView.invalidate()

                        return true
                    }
                })
            }
        },
        update = { mapView ->
            if (selectedLat != null && selectedLon != null) {
                val point = GeoPoint(selectedLat, selectedLon)
                mapView.controller.setCenter(point)

                markerHolder[0]?.let { mapView.overlays.remove(it) }

                val marker = Marker(mapView).apply {
                    position = point
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Selected location"
                }

                markerHolder[0] = marker
                mapView.overlays.add(marker)
                mapView.invalidate()
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose { }
    }
}