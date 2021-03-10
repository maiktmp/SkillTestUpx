package mx.com.maiktmp.skilltestupx.ui.home.view.interfaces

import com.google.android.gms.maps.model.LatLng

interface MapView {

    fun displayMarkers(markers: List<LatLng>)

    fun handleUnsuccessful(message: String?)

}