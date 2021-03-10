package mx.com.maiktmp.skilltestupx.base

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import mx.com.maiktmp.skilltestupx.ui.models.GenericResponse

object FireStoreRepository {
    private val db = Firebase.firestore

    fun attemptStoreLocation(location: LatLng) {
        db.collection("locations")
            .add(location)
            .addOnSuccessListener { _ ->
                Log.d(this::class.java.name, "Location Updated")
            }
            .addOnFailureListener { e ->
                Log.w(this::class.java.name, "Error adding document", e)
            }
    }


    fun retrieveStoredLocations(cb: (GenericResponse<List<LatLng>>) -> Unit) {
        db.collection("locations")
            .get()
            .addOnSuccessListener { documents ->
                val list = documents.map {
                    LatLng(
                        it.data["latitude"] as Double,
                        it.data["longitude"] as Double
                    )
                }
                cb(GenericResponse(success = true, data = list))
            }
            .addOnFailureListener { exception ->
                cb(GenericResponse(success = false))
                Log.w(this::class.java.name, "Error getting documents: ", exception)
            }
    }


}