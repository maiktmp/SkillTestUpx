package mx.com.maiktmp.skilltestupx.ui.home.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.databinding.ItemMovieBinding
import mx.com.maiktmp.skilltestupx.ui.models.Movie
import mx.com.maiktmp.web.ApiServer

class MovieAdapter(val items: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        val vBind = holder.vBind
        vBind.movie = movie

        Glide.with(vBind.ivMovie)
            .load("${ApiServer.imagesThumbnail}${movie.backdropPath}")
            .into(vBind.ivMovie)

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vBind = ItemMovieBinding.bind(itemView)
    }
}