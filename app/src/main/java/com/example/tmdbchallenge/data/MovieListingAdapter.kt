package com.example.tmdbchallenge.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbchallenge.MainActivityViewModel
import com.example.tmdbchallenge.databinding.MovieItemBinding
import com.example.tmdbchallenge.utilities.ImageHelper

class MovieListingAdapter(private val listener: Listener, private val viewModel: MainActivityViewModel) :
    RecyclerView.Adapter<MovieListingAdapter.MovieListingViewHolder>() {

    private val movies: MutableList<MovieListing> = mutableListOf()

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieListingViewHolder {
        val binding = MovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieListingViewHolder(binding, listener, viewModel)
    }

    override fun onBindViewHolder(holder: MovieListingViewHolder, position: Int) {
        if (position < movies.size) {
            holder.bind(movies[position])
        }
    }

    override fun getItemId(position: Int): Long {
        return movies[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<MovieListing>) {
        if (newList.isNotEmpty()) {
            movies.clear()
            movies.addAll(newList)
            notifyDataSetChanged()
        }
    }

    class MovieListingViewHolder(
        private val binding: MovieItemBinding,
        private val listener: Listener,
        private val viewModel: MainActivityViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieListing: MovieListing) {
            if (movieListing.poster_path != null && movieListing.poster_path != "") {
                val baseUrl = viewModel.getPosterUrl()
                ImageHelper.loadImage(baseUrl + movieListing.poster_path, binding.poster)
            }
            binding.movieName.text = movieListing.title
            binding.releaseDate.text = movieListing.release_date

            binding.root.setOnClickListener {
                listener.onMovieClicked(movieListing.id)
            }
        }
    }

    interface Listener {
        fun onMovieClicked(id: Int)
    }

}
