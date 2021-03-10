package mx.com.maiktmp.web.api


import mx.com.maiktmp.web.ApiServer
import mx.com.maiktmp.web.api.models.pagination.MoviePagerAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("${ApiServer.name}movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MoviePagerAPI>
}