package mx.com.maiktmp.database.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import mx.com.maiktmp.database.entities.MovieDB
import mx.com.maiktmp.database.entities.MoviePagerDB

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg movie: MovieDB): Completable

    @Transaction
    @Query("SELECT * FROM movie WHERE page= :page")
    fun findByPage(page: Int): Maybe<List<MovieDB>>

    @Transaction
    @Query("DELETE FROM movie")
    fun deleteAll(): Completable

}