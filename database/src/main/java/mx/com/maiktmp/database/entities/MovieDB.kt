package mx.com.maiktmp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieDB(

    @PrimaryKey
    val id: Long? = null,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    val title: String?,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,

    var page: Int?
)