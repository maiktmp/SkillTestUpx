package mx.com.maiktmp.web.api.models.pagination

import com.google.gson.annotations.SerializedName
import mx.com.maiktmp.web.api.models.MovieAPI

data class MoviePagerAPI(
    val page: Int,

    val results: List<MovieAPI>,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("total_pages")
    val totalPages: Int
)
