package com.example.tmdbchallenge

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbchallenge.data.MovieListingAdapter
import com.example.tmdbchallenge.databinding.ActivityMainBinding
import com.example.tmdbchallenge.utilities.OnlineHelper


class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieListingAdapter
    private var isConfigured = false

    private val adapterListener = object : MovieListingAdapter.Listener {
        override fun onMovieClicked(id: Int) {
            viewModel.updateActiveMovie(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.movieListRecyclerView
        adapter = MovieListingAdapter(adapterListener)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        initLivedata()
        initListeners(recyclerView)
        configureApp()
        refreshData()
    }

    private fun configureApp() {
        if (OnlineHelper.isOnline(this) && !isConfigured) {
            viewModel.configure(this)
        }
    }

    override fun onResume() {
        configureApp()
        refreshData()
        super.onResume()
    }

    private fun initLivedata() {
        viewModel.movieListLiveData.observe(this) { list ->
            if (isConfigured)
                adapter.update(list)
        }
        viewModel.isLoading.observe(this) { loading ->
            if (loading)
                binding.loadingIndicator.visibility = View.VISIBLE
            else binding.loadingIndicator.visibility = View.GONE
        }
        viewModel.configLoading.observe(this) { loading ->
            if (loading)
                binding.loadingIndicator.visibility = View.VISIBLE
            else {
                binding.loadingIndicator.visibility = View.GONE
                isConfigured = true
            }
        }
        viewModel.activeMovieId.observe(this) { id ->
            if (id > 0) {
                showMovieDetails(id)
            }
        }
    }


    private fun refreshData() {
        if (OnlineHelper.isOnline(this)) {
            if (viewModel.getMovieList(this)) Toast.makeText(
                this@MainActivity,
                "All data loaded.",
                Toast.LENGTH_SHORT
            ).show()
        } else OnlineHelper.showOnlineDialog(this)
    }

    private fun initListeners(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    refreshData()
                }
            }
        })
    }

    private fun showMovieDetails(id: Int) {
        val movieDetailFragment =
            DetailFragment.newInstance(id, viewModel.repository, object : OnClosedListener {
                override fun onCancel() {
                    viewModel.clearActiveMovie()
                }

                override fun onDismiss() {
                    //do nothing - the active movie will still be saved
                }
            })
        movieDetailFragment.show(supportFragmentManager, null)
    }

}