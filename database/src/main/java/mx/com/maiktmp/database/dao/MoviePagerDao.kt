package mx.com.maiktmp.database.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import mx.com.maiktmp.database.entities.MovieDB
import mx.com.maiktmp.database.entities.MoviePagerDB

@Dao
interface MoviePagerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(moviePager: MoviePagerDB): Completable

    @Transaction
    @Query("SELECT * FROM movie_pager WHERE page= :page LIMIT 1")
    fun findByPage(page: Int): Maybe<MoviePagerDB>

    @Transaction
    @Query("DELETE FROM movie_pager")
    fun deleteAll(): Completable

}