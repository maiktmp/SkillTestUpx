package mx.com.maiktmp.skilltestupx.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.gson.Gson


object Extensions {

    //Toast
    fun Context?.displayToast(message: String?, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }


    //    Visibility
    fun View.showView() {
        this.visibility = View.VISIBLE
    }

    fun View.hideView() {
        this.visibility = View.GONE
    }


    //Model conversion
    fun <T> Any.convert(cl: Class<T>): T {
        val gson = Gson()
        val data = gson.toJsonTree(this).asJsonObject
        return gson.fromJson(data, cl)
    }

    fun <T> List<Any>.convertToList(model: Class<T>): List<T> = this.map { it.convert(model) }


}