package mx.com.maiktmp.skilltestupx.ui.home.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mx.com.maiktmp.database.dao.MovieDao
import mx.com.maiktmp.database.dao.MoviePagerDao
import mx.com.maiktmp.database.entities.MovieDB
import mx.com.maiktmp.database.entities.MoviePagerDB
import mx.com.maiktmp.skilltestupx.ui.models.GenericResponse
import mx.com.maiktmp.skilltestupx.ui.models.Movie
import mx.com.maiktmp.skilltestupx.ui.models.pagination.MoviePager
import mx.com.maiktmp.skilltestupx.utils.Extensions.convert
import mx.com.maiktmp.skilltestupx.utils.Extensions.convertToList
import mx.com.maiktmp.web.api.ApiConnection

class MoviesDbRepository(private val movieDao: MovieDao, private val moviePagerDao: MoviePagerDao) {

    fun getDbMoviePager(
        page: Int = 1,
        cbResult: (GenericResponse<MoviePager?>) -> Unit
    ): Disposable {
        return moviePagerDao.findByPage(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    cbResult(
                        GenericResponse(
                            success = true,
                            data = it.convert(MoviePager::class.java)
                        )
                    )
                },
                {
                    cbResult(GenericResponse())
                }, {
                    cbResult(GenericResponse(success = true, data = MoviePager()))
                })
    }

    fun getDbMovies(
        page: Int = 1,
        cbResult: (GenericResponse<ArrayList<Movie>?>) -> Unit
    ): Disposable {
        return movieDao.findByPage(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    cbResult(
                        GenericResponse(
                            success = true,
                            data = ArrayList<Movie>().apply {
                                addAll(it.convertToList(Movie::class.java))
                            }
                        )
                    )
                },
                {
                    cbResult(GenericResponse())
                }, {
                    cbResult(GenericResponse(success = true, data = ArrayList()))
                })
    }

    fun storeMoviePager(
        moviePager: MoviePagerDB,
        cbResult: (GenericResponse<Boolean>) -> Unit
    ): Disposable {

        return moviePagerDao.upsert(moviePager)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cbResult(GenericResponse(success = true, data = true)) },
                { cbResult(GenericResponse(success = false, data = false)) })
    }

    fun storeMovies(
        movies: Array<MovieDB>,
        cbResult: (GenericResponse<Boolean>) -> Unit
    ): Disposable {

        return movieDao.upsert(*movies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cbResult(GenericResponse(success = true, data = true)) },
                { cbResult(GenericResponse(success = false, data = false)) })
    }
}