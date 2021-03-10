package mx.com.maiktmp.web.api

import android.util.Log
import com.google.gson.GsonBuilder
import mx.com.maiktmp.web.ApiServer
import mx.com.maiktmp.web.BuildConfig
import mx.com.maiktmp.web.api.models.pagination.MoviePagerAPI
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object ApiConnection {

    private lateinit var service: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url()
                val url: HttpUrl = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .addQueryParameter("language", "es")
                    .build()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }

        val gson = GsonBuilder()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiServer.name)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClientBuilder.build())
            .build()

        service = retrofit.create(ApiService::class.java)

    }


    private fun <T> enqueueCall(call: Call<T>, cbResult: (status: Boolean, result: T?) -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e(this::class.java.name, "Error Response: " + t.message)
                cbResult(false, null)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    cbResult(true, response.body()!!)
                } else {
                    cbResult(false, null)
                }
            }
        })
    }

    fun getPopularMovies(page: Int, cbResult: (status: Boolean, result: MoviePagerAPI?) -> Unit) {
        enqueueCall(service.getPopularMovies(page), cbResult)
    }
}