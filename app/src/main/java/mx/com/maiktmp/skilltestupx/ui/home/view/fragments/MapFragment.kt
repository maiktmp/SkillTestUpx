package mx.com.maiktmp.skilltestupx.ui.home.view.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.base.FireStoreRepository
import mx.com.maiktmp.skilltestupx.databinding.FragmentMapBinding
import mx.com.maiktmp.skilltestupx.ui.home.view.HomeActivity
import mx.com.maiktmp.skilltestupx.ui.home.presenter.MapPresenter
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.MapView
import mx.com.maiktmp.skilltestupx.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestupx.utils.maps.MapHelper


class MapFragment : Fragment(), MapHelper.MapHandler, MapView {

    lateinit var vBind: FragmentMapBinding
    lateinit var homeActivity: HomeActivity


    private val mapHelper: MapHelper by lazy {
        MapHelper(activity as Activity, this)
    }

    private val presenter: MapPresenter by lazy {
        MapPresenter(homeActivity, FireStoreRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vBind = FragmentMapBinding.inflate(inflater, container, false)
        presenter.attachView(this, lifecycle)
        setupMap()
        return vBind.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = activity as HomeActivity
    }

    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.f_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(mapHelper)
    }

    override fun onMapCompletelyConfigured(gMap: GoogleMap?) {
        presenter.retrieveMarkers()
    }

    override fun displayMarkers(markers: List<LatLng>) {
        val arrayMarkers = markers.toTypedArray()
        mapHelper.displayMarkers(*arrayMarkers)
        mapHelper.centerCameraIn(*arrayMarkers)
    }

    override fun handleUnsuccessful(message: String?) {
        context?.let {
            it.displayToast(message, Toast.LENGTH_LONG)
        }
    }


}