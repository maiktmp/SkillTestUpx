package mx.com.maiktmp.skilltestupx.ui.home.presenter

import android.content.Context
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.base.BasePresenter
import mx.com.maiktmp.skilltestupx.base.FireStoreRepository
import mx.com.maiktmp.skilltestupx.ui.home.data.MoviesDbRepository
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.MapView

class MapPresenter(
    private val context: Context?,
    private val fbRepository: FireStoreRepository
) :
    BasePresenter<MapView>() {

    fun retrieveMarkers() {
        fbRepository.retrieveStoredLocations() {
            if (!it.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_get_locations))
                return@retrieveStoredLocations
            }
            view()?.displayMarkers(it.data!!)
        }
    }

}