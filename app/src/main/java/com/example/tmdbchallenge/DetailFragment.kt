package com.example.tmdbchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbchallenge.data.MovieDetails
import com.example.tmdbchallenge.data.Repository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(id: Int, repo: Repository): DetailFragment {
            val detailFragment = DetailFragment()
            detailFragment.viewModel = DetailViewModel(repo, id)
            return detailFragment
        }
    }

    lateinit var movie: State<MovieDetails?>
    lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                MovieDetailScreen()
            }
        }
    }

    @Composable
    private fun MovieDetailScreen() {
        movie = viewModel.movieDetailsLiveData.observeAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(colorResource(id = R.color.yellow_100))
                .fillMaxHeight(0.75F)
                .fillMaxWidth(1F)
                .padding(12.dp)
                .verticalScroll(rememberScrollState(), enabled = true)
            //No native scrollbar support at this time unfortunately!
        ) {

            //Requirements: movie title, backdrop image,
            // cast images
            // related movie images

            movie.value?.let { Header(it) }
            //ScrollingImageList(cast) //scrolling lazylist
            //ScrollingImageList(otherimages) //scrolling lazylist
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Header(movie: MovieDetails) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(0.dp, 8.dp)
        ) {
            if (movie.backdrop_path != null && movie.backdrop_path != "")
                GlideImage(
                    model = movie.backdrop_path,
                    contentDescription = "Movie backdrop",
                    Modifier.fillMaxWidth(1F)
                )
            else Image(
                painter = painterResource(id = R.drawable.baseline_movie),
                contentDescription = "Default movie icon",
                Modifier.fillMaxWidth(1F)
            )
            if (movie.title != "") Text(
                text = movie.title, style = MaterialTheme.typography.h4
            )
        }
    }
}
