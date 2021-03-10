package mx.com.maiktmp.skilltestupx.ui.home.data

import mx.com.maiktmp.skilltestupx.ui.models.GenericResponse
import mx.com.maiktmp.skilltestupx.ui.models.pagination.MoviePager
import mx.com.maiktmp.skilltestupx.utils.Extensions.convert
import mx.com.maiktmp.web.api.ApiConnection

class MoviesApiRepository(private val api: ApiConnection) {

    fun getApiPopularMovies(page: Int = 1, cbResult: (GenericResponse<MoviePager?>) -> Unit) {
        api.getPopularMovies(page) { status, result ->
            cbResult(
                GenericResponse(
                    status,
                    data = result?.convert(MoviePager::class.java)
                )
            )
        }
    }
}