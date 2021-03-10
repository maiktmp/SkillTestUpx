package mx.com.maiktmp.skilltestupx.ui.home.presenter

import android.content.Context
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.base.BasePresenter
import mx.com.maiktmp.skilltestupx.ui.home.data.FireStoreRepository
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.ImagesView
import java.io.File

class ImagesPresenter(
    private val context: Context?,
    private val repository: FireStoreRepository
) : BasePresenter<ImagesView>() {

    fun sendImages(vararg files: File) {
        view()?.showProgress()
        repository.uploadFile(*files) {
            view()?.hideProgress()
            if (!it.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_upload_file))
                return@uploadFile
            }
            view()?.completeUploadFiles()
        }
    }

}