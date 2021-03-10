package mx.com.maiktmp.skilltestupx.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import mx.com.maiktmp.skilltestupx.base.FireStoreRepository
import mx.com.maiktmp.skilltestupx.utils.Codes


class LocationService : LifecycleService() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var isLive = false

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Codes.ACTION_START_LOCATION_SERVICE -> {
                    Log.d(LocationService::class.java.name, "START_SERVICE")
                    if (!isLive) startTrackingLocation()
                    isLive = true
                }

                Codes.ACTION_STOP_LOCATION_SERVICE -> {
                    Log.d(LocationService::class.java.name, "STOP_SERVICE")
                    isLive = false
                    stopService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService() {
        if (::fusedLocationProviderClient.isInitialized)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }


    @SuppressLint("MissingPermission")
    private fun startTrackingLocation() {
        updateLocationTracking()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking() {
        val request = LocationRequest().apply {
            interval = 30 * 60000

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult?) {
            super.onLocationResult(location)
            location?.let {
                val lastLocation = LatLng(
                    location.lastLocation.latitude,
                    location.lastLocation.longitude
                )
                FireStoreRepository.attemptStoreLocation(lastLocation)
            }
        }
    }
}