package mx.com.maiktmp.skilltestupx.ui.home.data

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import mx.com.maiktmp.skilltestupx.ui.models.GenericResponse
import java.io.File
import java.io.FileInputStream
import java.sql.SQLOutput

object FireStoreRepository {

    private val storage = Firebase.storage

    fun uploadFile(vararg files: File, cb: (GenericResponse<List<File>>) -> Unit) {
        val storageRef = storage.reference
        var success = false
        val filesError = ArrayList<File>()

        for (file in files) {
            val mountainsRef = storageRef.child(file.name)
            val mountainImagesRef = storageRef.child(file.path)

            val stream = FileInputStream(file)
            val uploadTask = mountainsRef.putStream(stream)
            uploadTask.addOnFailureListener {
                success = false
                filesError.add(file)
                Log.w(this::class.java.name, "Error adding document", it)
            }.addOnSuccessListener {
                success = true
                Log.d(this::class.java.name, "File send")
            }
        }
        cb(GenericResponse(success = success, data = filesError))
    }

}