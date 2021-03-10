package mx.com.maiktmp.skilltestupx.ui.models.pagination

import com.google.gson.annotations.SerializedName
import mx.com.maiktmp.skilltestupx.ui.models.Movie

data class MoviePager(
    val page: Int? = null,

    var results: ArrayList<Movie>? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null,

    @SerializedName(value = "total_pages", alternate = ["totalPages"])
    val totalPages: Int? = null
)
