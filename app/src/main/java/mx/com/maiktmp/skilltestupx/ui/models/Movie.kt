package mx.com.maiktmp.skilltestupx.ui.models

import com.google.gson.annotations.SerializedName

data class Movie(

    val id: Long,

    val adult: Boolean,

    @SerializedName(value = "backdrop_path", alternate = ["backdropPath"])
    val backdropPath: String,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    val overview: String,

    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName(value = "release_date", alternate = ["releaseDate"])
    val releaseDate: String,

    val title: String,

    val video: Boolean,

    @SerializedName(value = "vote_average", alternate = ["voteAverage"])
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
) {
    fun calculateStars(): Float {
        return ((voteAverage * 5) / (10)).toFloat()
    }
}
