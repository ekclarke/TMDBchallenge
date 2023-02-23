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


class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieListingAdapter

    private val adapterListener = object : MovieListingAdapter.Listener {
        override fun onMovieClicked(id: Int) {
            val movieDetailFragment = DetailFragment.newInstance(id, viewModel.repository)
            movieDetailFragment.show(supportFragmentManager, null)
        }
    }

    //TODO: action bar design
    //TODO: bind rating instead of title?
    //TODO: parse date so it looks nicer
    //TODO: marquee scrolling or text resizing?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.movieListRecyclerView
        adapter = MovieListingAdapter(adapterListener, viewModel)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        initLivedata()
        refreshData()
        initListeners(recyclerView)
    }

    private fun initLivedata() {
        viewModel.movieListLiveData.observe(this) { list ->
            adapter.update(list)
        }
        viewModel.isLoading.observe(this) { loading ->
            if (loading)
                binding.loadingIndicator.visibility = View.VISIBLE
            else binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun refreshData() {
        viewModel.getMovieList()
        if (viewModel.getMovieList()) Toast.makeText(
            this@MainActivity,
            "All data loaded.",
            Toast.LENGTH_SHORT
        ).show()
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
}