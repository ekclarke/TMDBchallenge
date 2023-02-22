package com.example.tmdbchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.tmdbchallenge.data.Cast
import com.example.tmdbchallenge.data.Image
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

    private lateinit var movie: State<MovieDetails?>
    lateinit var viewModel: DetailViewModel
    private lateinit var castList: State<List<Cast>>
    private lateinit var moviePosters: State<List<Image>>

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
        castList = viewModel.castListLiveData.observeAsState(initial = listOf())
        moviePosters = viewModel.moviePostersLiveData.observeAsState(initial = listOf())

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
            movie.value?.let { Header(it) }
            ScrollingCastList(castList.value)
            ScrollingPostersList(moviePosters.value)
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Header(movie: MovieDetails) {
        Box(
            modifier = Modifier.fillMaxWidth(1F)
        ) {
            if (movie.backdrop_path != null && movie.backdrop_path != "") {
                val baseUrl = viewModel.getBackdropUrl()
                GlideImage(
                    model = baseUrl + movie.backdrop_path,
                    contentDescription = "Movie backdrop",
                    Modifier.fillMaxWidth(1F)
                )
            } else Image(
                painter = painterResource(id = R.drawable.baseline_movie),
                contentDescription = "Default movie icon",
                Modifier.fillMaxWidth(1F)
            )
            if (movie.title != "") Text(
                text = movie.title, style = MaterialTheme.typography.h4
            )
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingCastList(castList: List<Cast>) {
        Text("Cast", style = MaterialTheme.typography.h4)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()
        ) {
            items(castList) { item ->
                val imageItem = viewModel.getCastImage(item.cast_id).first()
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (imageItem.file_path != "") {
                        val baseUrl = viewModel.getProfileUrl()
                        GlideImage(
                            model = baseUrl + imageItem.file_path,
                            contentDescription = "Cast image",
                            Modifier.fillMaxWidth(.8F)
                        )
                    } else Image(
                        painter = painterResource(id = R.drawable.baseline_person),
                        contentDescription = "Default cast icon",
                        Modifier.fillMaxWidth(.8F)
                    )
                    if (item.name != "") Text(
                        text = item.name, style = MaterialTheme.typography.body1
                    )
                    if (item.character != "")
                        Text(text = item.character, style = MaterialTheme.typography.body2)
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingPostersList(postersList: List<Image>) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()
        ) {
            items(postersList) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (item.file_path != "") {
                        val baseUrl = viewModel.getPosterUrl()
                        GlideImage(
                            model = baseUrl + item.file_path,
                            contentDescription = "Movie poster",
                            Modifier.fillMaxWidth(.8F)
                        )
                    } else Image(
                        painter = painterResource(id = R.drawable.baseline_poster),
                        contentDescription = "Default poster icon",
                        Modifier.fillMaxWidth(.8F)
                    )
                }
            }
        }
    }
}


