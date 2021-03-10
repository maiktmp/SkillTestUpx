package mx.com.maiktmp.skilltestupx.ui.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.com.maiktmp.database.DBSkillTestUpx
import mx.com.maiktmp.skilltestupx.databinding.FragmentMoviesBinding
import mx.com.maiktmp.skilltestupx.ui.home.data.MoviesApiRepository
import mx.com.maiktmp.skilltestupx.ui.home.data.MoviesDbRepository
import mx.com.maiktmp.skilltestupx.ui.home.presenter.MoviesPresenter
import mx.com.maiktmp.skilltestupx.ui.home.view.adapter.MovieAdapter
import mx.com.maiktmp.skilltestupx.ui.home.view.interfaces.MoviesView
import mx.com.maiktmp.skilltestupx.ui.models.Movie
import mx.com.maiktmp.skilltestupx.utils.Extensions.displayToast
import mx.com.maiktmp.skilltestupx.utils.Extensions.hideView
import mx.com.maiktmp.skilltestupx.utils.Extensions.showView
import mx.com.maiktmp.web.api.ApiConnection


class MovieFragment : Fragment(), MoviesView {

    private val presenter: MoviesPresenter by lazy {
        MoviesPresenter(
            context,
            MoviesApiRepository(ApiConnection),
            MoviesDbRepository(DBSkillTestUpx.db.movieDao(), DBSkillTestUpx.db.moviePagerDao())
        )
    }

    lateinit var vBind: FragmentMoviesBinding
    private var isLoadingMore = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vBind = FragmentMoviesBinding.inflate(inflater, container, false)
        presenter.attachView(this, lifecycle)
        presenter.getPopularMovies()
        return vBind.root
    }

    private fun setupRecyclerView() {

        vBind.rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = this@apply.layoutManager!! as LinearLayoutManager

                    val visibleItemCount: Int = layoutManager.childCount
                    val totalItemCount: Int = layoutManager.itemCount
                    val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    presenter.loadMoreMovies(
                        isLoadingMore,
                        visibleItemCount,
                        totalItemCount,
                        firstVisibleItemPosition
                    )
                }

            }

            addOnScrollListener(scrollListener)
        }
    }

    private fun setupAdapter(movies: ArrayList<Movie>) {
        val adapter = MovieAdapter(movies)
        vBind.rvMovies.adapter = adapter
    }

    override fun listMovies(movies: ArrayList<Movie>) {
        if (movies.isEmpty()) {
            vBind.lblEmpty.showView()
            return
        }
        setupRecyclerView()
        setupAdapter(movies)
    }

    override fun handleUnsuccessful(message: String?) {
        context.let {
            it.displayToast(message, Toast.LENGTH_SHORT)
        }
    }

    override fun showMoreMoviesProgress() {
        isLoadingMore = true
        vBind.cvLoading.showView()
    }

    override fun hideMoreMoviesProgress() {
        vBind.cvLoading.hideView()
    }

    override fun loadMoreMovies(movies: ArrayList<Movie>) {
        isLoadingMore = false
        val adapter = vBind.rvMovies.adapter as MovieAdapter
        val prevSize = adapter.items.size
        adapter.items.addAll(movies)
        val currentSize = adapter.items.size
        adapter.notifyItemRangeInserted(prevSize, currentSize)
    }

    override fun handleUnsuccessfulLoadMore(message: String?) {
        isLoadingMore = false
    }

    override fun showProgress() {
        vBind.pbLoading.showView()
    }

    override fun hideProgress() {
        vBind.pbLoading.hideView()
    }

}