package mx.com.maiktmp.skilltestupx.ui.home.data

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.observable.ObservableFromCallable
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.skilltestupx.ui.models.GenericResponse
import java.io.File
import java.io.FileInputStream
import java.sql.SQLOutput
import java.util.*
import kotlin.collections.ArrayList

object FireStoreRepository {

    private val storage = Firebase.storage

    @SuppressLint("CheckResult")
    fun uploadFile(files: ArrayList<File>, cb: (GenericResponse<List<File>>) -> Unit) {
        val storageRef = storage.reference
        var success = false
        val filesError = ArrayList<File>()

        Observable.fromIterable(files)
            .flatMap { file ->
                return@flatMap Observable.create<Boolean> { emmiter ->
                    val mountainsRef = storageRef.child(file.name)
                    val mountainImagesRef = storageRef.child(file.path)
                    val stream = FileInputStream(file)
                    val uploadTask = mountainsRef.putStream(stream)
                    uploadTask.addOnFailureListener {
                        filesError.add(file)
                        emmiter.onNext(false)
                        emmiter.onComplete()
                    }.addOnSuccessListener {
                        emmiter.onNext(true)
                        emmiter.onComplete()
                    }
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success = it },
                { cb(GenericResponse(success = false, data = null)) },
                { cb(GenericResponse(success = success, data = filesError)) }
            )

//        for (file in files) {
//
//            val mountainsRef = storageRef.child(file.name)
//            val mountainImagesRef = storageRef.child(file.path)
//
//            val stream = FileInputStream(file)
//            val uploadTask = mountainsRef.putStream(stream)
//            uploadTask.addOnFailureListener {
//                success = false
//                filesError.add(file)
//                Log.w(this::class.java.name, "Error adding document", it)
//            }.addOnSuccessListener {
//                success = true
//                Log.d(this::class.java.name, "File send")
//            }
//        }

    }

}