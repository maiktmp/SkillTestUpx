package mx.com.maiktmp.skilltestupx.utils.maps

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import mx.com.maiktmp.skilltestupx.R


class MapHelper(
    private val activity: Activity,
    private val mapHandler: MapHandler
) : OnMapReadyCallback {


    lateinit var gMap: GoogleMap

    override fun onMapReady(gMap: GoogleMap?) {
        this.gMap = gMap!!
        mapHandler.onMapCompletelyConfigured(gMap)
        setupMapUi()
    }

    fun displayMarkers(
        vararg locations: LatLng,
        tag: Any? = null
    ) {
        for (location in locations) {
            gMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.round_account_button_with_user_inside))
            ).tag = tag
        }
    }

    fun centerCameraIn(vararg positions: LatLng) {
        val builder = LatLngBounds.Builder()
        for (position in positions) {
            builder.include(position)
        }
        val bounds = builder.build()
        val padding = 300
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        gMap.animateCamera(cu)
    }

//    private fun prepareMarkerIcon(): Bitmap? {
//        val bDrawable = ContextCompat
//            .getDrawable(activity, R.drawable.ic_baseline_account_circle_24) as BitmapDrawable?
//        return if (bDrawable != null) Bitmap.createScaledBitmap(
//            bDrawable.bitmap,
//            toPx(32f) as Int,
//            toPx(46f) as Int,
//            false
//        ) else null
//    }

//    fun toPx(dp: Float): Float {
//        val resources: Resources = activity.resources
//        val metrics: DisplayMetrics = resources.displayMetrics
//        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
//    }

    fun centerCameraIn(position: LatLng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }

    private fun setupMapUi() {
        gMap.uiSettings.isZoomControlsEnabled = true
    }

    interface MapHandler {
        fun onMapCompletelyConfigured(gMap: GoogleMap?)
    }
}