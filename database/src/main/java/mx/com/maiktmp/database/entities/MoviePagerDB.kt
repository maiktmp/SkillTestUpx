package mx.com.maiktmp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_pager")
data class MoviePagerDB(

    @PrimaryKey
    val page: Int,

    @ColumnInfo(name = "total_results")
    val totalResults: Int,

    @ColumnInfo(name = "total_pages")
    val totalPages: Int

)