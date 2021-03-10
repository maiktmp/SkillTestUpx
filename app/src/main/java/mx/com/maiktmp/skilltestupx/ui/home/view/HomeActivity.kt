package mx.com.maiktmp.skilltestupx.ui.home.view

import android.Manifest
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.databinding.ActivityHomeBinding
import mx.com.maiktmp.skilltestupx.service.LocationService
import mx.com.maiktmp.skilltestupx.ui.home.view.fragments.ImagesFragment
import mx.com.maiktmp.skilltestupx.ui.home.view.fragments.MapFragment
import mx.com.maiktmp.skilltestupx.ui.home.view.fragments.MovieFragment
import mx.com.maiktmp.skilltestupx.utils.Codes
import mx.com.maiktmp.skilltestupx.utils.Extensions.displayToast
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class HomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    ImagesFragment.FragmentImagesHandler {

    private val vBind: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBind.bottomAppBar
        checkPermissions()
        setupMainFragment()
        setupBottomNavigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val file: File = ImagePicker.getFile(data)!!
            val imageFragment = findFragment(Codes.FRAGMENT_IMAGES) as ImagesFragment
            imageFragment.addImage(file)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        sendCommandToService(Codes.ACTION_STOP_LOCATION_SERVICE)
    }

    private fun sendCommandToService(action: String) {
        Intent(this, LocationService::class.java).also {
            it.action = action
            startService(it)
        }
    }

    private fun setupMainFragment() {
        if (supportFragmentManager.findFragmentByTag(Codes.FRAGMENT_MOVIES) != null) return
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_root, MovieFragment(), Codes.FRAGMENT_MOVIES)
            .commit()
    }


    private fun replaceFragment(tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        val fragment = when (tag) {
            Codes.FRAGMENT_MOVIES -> MovieFragment()
            Codes.FRAGMENT_IMAGES -> ImagesFragment()
            Codes.FRAGMENT_MAPS -> MapFragment()
            else -> null
        } ?: return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_root, fragment, tag)
            .commit()
    }

    private fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }


    private fun setupBottomNavigation() {
        vBind.bottomAppBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.i_movies -> {
                    replaceFragment(Codes.FRAGMENT_MOVIES)
                    true
                }
                R.id.i_maps -> {
                    replaceFragment(Codes.FRAGMENT_MAPS)
                    true
                }
                R.id.i_images -> {
                    replaceFragment(Codes.FRAGMENT_IMAGES)
                    true
                }
                else -> false
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun checkPermissions() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            sendCommandToService(Codes.ACTION_START_LOCATION_SERVICE)
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.denied_location),
                Codes.PERMISSION_FINE_LOCATION,
                *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == Codes.PERMISSION_FINE_LOCATION) sendCommandToService(Codes.ACTION_START_LOCATION_SERVICE)
    }

    private fun chooseImageGallery() {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)
            .start()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        displayToast(getString(R.string.denied_location), Toast.LENGTH_LONG)
    }

    override fun onSelectImage() {
        chooseImageGallery()
    }

}