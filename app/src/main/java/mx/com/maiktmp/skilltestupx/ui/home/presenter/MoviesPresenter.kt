package mx.com.maiktmp.skilltestupx.ui.home.presenter

import android.content.Context
import mx.com.maiktmp.database.entities.MovieDB
import mx.com.maiktmp.database.entities.MoviePagerDB
import mx.com.maiktmp.skilltestupx.R
import mx.com.maiktmp.skilltestupx.base.BasePresenter
import mx.com.maiktmp.skilltestupx.ui.home.data.MoviesApiRepository
import mx.com.maiktmp.skilltestupx.ui.home.data.MoviesDbRepository
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.MoviesView
import mx.com.maiktmp.skilltestupx.ui.models.Movie
import mx.com.maiktmp.skilltestupx.ui.models.pagination.MoviePager
import mx.com.maiktmp.skilltestupx.utils.Extensions.convert
import mx.com.maiktmp.skilltestupx.utils.NetworkUtil

class MoviesPresenter(
    private val context: Context?,
    private val apiRepository: MoviesApiRepository,
    private val dbRepository: MoviesDbRepository,
) : BasePresenter<MoviesView>() {

    private lateinit var moviePager: MoviePager
    private val pageSize = 20


    fun getPopularMovies() {
        view()?.showProgress()
        if (NetworkUtil.isNetworkAvailable(context)) {
            loadApiMovies()
        } else {
            view()?.handleUnsuccessful(context?.getString(R.string.error_network))
            loadDbMovies()
        }
    }


    fun loadMoreMovies(
        isLoadingMore: Boolean,
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (!::moviePager.isInitialized || moviePager.totalPages == null || moviePager.page == null) {
            return
        }

        if (!isLoadingMore && moviePager.page!! <= moviePager.totalPages!!) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {

                if (NetworkUtil.isNetworkAvailable(context)) {
                    loadMoreApiMovies()
                } else {
                    loaMoreDbMovies()
                }

            }
        }
    }


    private fun loadDbMovies() {
        view()?.showProgress()
        dbRepository.getDbMoviePager { grMoviePager ->

            if (!grMoviePager.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_get_local_movies))
                view()?.hideProgress()
                return@getDbMoviePager
            }

            dbRepository.getDbMovies { grMovies ->
                if (!grMovies.success) {
                    view()?.handleUnsuccessful(context?.getString(R.string.error_get_local_movies))
                    view()?.hideProgress()
                    return@getDbMovies
                }

                moviePager = grMoviePager.data!!
                view()?.listMovies(grMovies.data!!)
                view()?.hideProgress()

            }.autoClear()
        }.autoClear()
    }

    private fun loadApiMovies() {
        view()?.showProgress()
        apiRepository.getApiPopularMovies {
            view()?.hideProgress()

            if (!it.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_get_movies))
                return@getApiPopularMovies
            }

            moviePager = it.data!!
            view()?.listMovies(it.data?.results!!)
            storeLocalMovies(it.data)
        }
    }


    private fun loadMoreApiMovies() {
        view()?.showMoreMoviesProgress()
        apiRepository.getApiPopularMovies(moviePager.page!! + 1) {
            view()?.hideMoreMoviesProgress()
            if (!it.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_get_movies))
                return@getApiPopularMovies
            }
            moviePager = it.data!!
            view()?.loadMoreMovies(it.data?.results!!)
            storeLocalMovies(it.data)
        }
    }

    private fun loaMoreDbMovies() {
        view()?.showMoreMoviesProgress()
        dbRepository.getDbMoviePager(moviePager.page!! + 1) { grMoviePager ->

            if (!grMoviePager.success) {
                view()?.handleUnsuccessful(context?.getString(R.string.error_get_local_movies))
                view()?.hideMoreMoviesProgress()
                return@getDbMoviePager
            }

            dbRepository.getDbMovies(moviePager.page!! + 1) { grMovies ->
                if (!grMovies.success) {
                    view()?.handleUnsuccessful(context?.getString(R.string.error_get_local_movies))
                    view()?.hideMoreMoviesProgress()
                    return@getDbMovies
                }

                moviePager = grMoviePager.data!!
                view()?.loadMoreMovies(grMovies.data!!)
                view()?.hideMoreMoviesProgress()

            }.autoClear()
        }.autoClear()
    }

    private fun storeLocalMovies(moviePager: MoviePager?) {
        if (moviePager == null) return

        dbRepository.storeMoviePager(
            MoviePagerDB(moviePager.page!!, moviePager.totalResults!!, moviePager.totalPages!!)
        ) {}.autoClear()

        val movies = moviePager.results?.map {
            MovieDB(
                it.id,
                it.backdropPath,
                it.releaseDate,
                it.title,
                it.voteAverage,
                moviePager.page
            )
        }

        movies?.let {
            dbRepository.storeMovies(it.toTypedArray()) {}.autoClear()
        }

    }
}