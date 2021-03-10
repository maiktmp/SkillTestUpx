package mx.com.maiktmp.skilltestupx.ui.home.view.interfaces

import mx.com.maiktmp.skilltestupx.ui.models.Movie

interface MoviesView {

    fun listMovies(movies: ArrayList<Movie>)

    fun handleUnsuccessful(message: String?)

    fun showMoreMoviesProgress()

    fun hideMoreMoviesProgress()

    fun loadMoreMovies(movies: ArrayList<Movie>)

    fun handleUnsuccessfulLoadMore(message: String?)

    fun showProgress()

    fun hideProgress()
}