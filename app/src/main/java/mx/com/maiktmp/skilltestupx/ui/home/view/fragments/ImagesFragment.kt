package mx.com.maiktmp.skilltestupx.ui.home.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.databinding.FragmentImagesBinding
import mx.com.maiktmp.skilltestupx.ui.home.data.FireStoreRepository
import mx.com.maiktmp.skilltestupx.ui.home.presenter.ImagesPresenter
import mx.com.maiktmp.skilltestupx.ui.home.presenter.MoviesPresenter
import mx.com.maiktmp.skilltestupx.ui.home.view.HomeActivity
import mx.com.maiktmp.skilltestupx.ui.home.view.adapter.ImageAdapter
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.ImagesView
import mx.com.maiktmp.skilltestupx.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestupx.utils.Extensions.hideView
import mx.com.maiktmp.skilltestupx.utils.Extensions.showView
import java.io.File

class ImagesFragment : Fragment(), ImagesView {

    private val presenter: ImagesPresenter by lazy {
        ImagesPresenter(context, FireStoreRepository)
    }

    lateinit var vBind: FragmentImagesBinding

    lateinit var activityHandler: FragmentImagesHandler

    lateinit var adapter: ImageAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vBind = FragmentImagesBinding.inflate(inflater, container, false)
        presenter.attachView(this, lifecycle)
        setupRecyclerView()
        setupAdapter()
        setupAddBtn()
        return vBind.root
    }

    fun addImage(file: File) {
        vBind.lblEmpty.hideView()
        vBind.btnUpload.isEnabled = true
        setupUploadBtn()

        val files = adapter.items
        files.add(file)
        adapter.notifyItemInserted(files.size)
    }

    private fun setupAddBtn() {
        vBind.btnAdd.setOnClickListener {
            activityHandler.onSelectImage()
        }
    }

    private fun setupUploadBtn() {
        vBind.btnUpload.setOnClickListener {
            presenter.sendImages(*adapter.items.toTypedArray())
            vBind.lblEmpty
        }
    }

    private fun setupRecyclerView() {
        vBind.rvImages.apply {
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setupAdapter() {
        adapter = ImageAdapter(ArrayList())
        vBind.rvImages.adapter = adapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityHandler = context as HomeActivity
    }


    interface FragmentImagesHandler {
        fun onSelectImage()
    }

    override fun handleUnsuccessful(message: String?) {
        context?.let {
            it.displayToast(message, Toast.LENGTH_LONG)
        }
    }

    override fun completeUploadFiles() {
        context?.let {
            it.displayToast(it.getString(R.string.success_send_files), Toast.LENGTH_LONG)
        }
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        vBind.lblEmpty.showView()
    }

    override fun showProgress() {
        vBind.pbLoading.showView()
        vBind.rvImages.hideView()
    }

    override fun hideProgress() {
        vBind.pbLoading.hideView()
        vBind.rvImages.showView()
    }

}