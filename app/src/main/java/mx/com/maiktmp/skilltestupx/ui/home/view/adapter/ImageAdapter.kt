package mx.com.maiktmp.skilltestupx.ui.home.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.databinding.ItemImageBinding
import java.io.File

class ImageAdapter(val items: ArrayList<File>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = items[position]
        val vBind = holder.vBind


        Glide.with(vBind.itemRoot)
            .load(image)
            .into(vBind.itemRoot)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vBind = ItemImageBinding.bind(itemView)
    }
}