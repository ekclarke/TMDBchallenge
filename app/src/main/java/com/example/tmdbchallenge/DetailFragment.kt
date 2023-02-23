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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbchallenge.data.*
import com.example.tmdbchallenge.utilities.DateHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class DetailFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(id: Int, repo: Repository): DetailFragment {
            val detailFragment = DetailFragment()
            detailFragment.viewModel = DetailViewModel(repo, id)
            return detailFragment
        }
    }

    private lateinit var movie: State<MovieDetails?>
    private lateinit var viewModel: DetailViewModel
    private lateinit var castList: State<List<Cast>>
    private lateinit var moviePosters: State<List<Image>>
    private lateinit var castImages: State<List<Image?>>

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
        castImages = viewModel.castImagesLiveData.observeAsState(initial = listOf())

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
    fun Header(movie: MovieDetails) {
        val gradient = Brush.horizontalGradient(
            0.0f to Color.Black,
            1.0f to Color.Transparent,
            startX = 0.0f,
            endX = 1000.0f
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1F)
        ) {
            if (movie.backdrop_path != null && movie.backdrop_path != "") {
                val imageUrl = "https://image.tmdb.org/t/p/w1280" + movie.backdrop_path
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Movie backdrop",
                    Modifier
                        .fillMaxWidth(1F)
                        .align(Alignment.TopCenter)
                )
            } else Image(
                painter = painterResource(id = R.drawable.baseline_movie),
                contentDescription = "Default movie icon",
                Modifier.fillMaxWidth(1F)
            )
            Column(
                Modifier
                    .background(gradient)
                    .fillMaxWidth(1F)
                    .fillMaxHeight(1F)
            ) {
                if (movie.title != "") Text(
                    text = movie.title,
                    style = MaterialTheme.typography.h5,
                    color = colorResource(id = R.color.blue_200),
                    modifier = Modifier
                        .fillMaxWidth(1F)
                        .fillMaxHeight(1F)
                )
                if (movie.status.toString() != "")
                    Text(
                        text = movie.status.toString().lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.h6,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier.fillMaxWidth(.8F)
                    )
                if (movie.release_date != "")
                    Text(
                        text = DateHelper.getFormattedDate(
                            movie.release_date,
                            LocalContext.current
                        ),
                        style = MaterialTheme.typography.h6,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier.fillMaxWidth(.8F)
                    )
                if (movie.runtime != 0)
                    Text(
                        text = movie.runtime.toString() + " minutes",
                        style = MaterialTheme.typography.h6,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier.fillMaxWidth(.8F)
                    )
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingCastList(castList: List<Cast>) {
        if (castList.isNotEmpty()) {
            Text("Cast", style = MaterialTheme.typography.h6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                //TODO: Look into loading placeholders
                //TODO: make castImages a parameter and/or mapping
                items(castList) { castMember ->
                    if (castImages.value.isNotEmpty()) {
                        val castImage = castImages.value[castMember.order]
                        if (castImage != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (castImage.file_path != "") {
                                    val url =
                                        "https://image.tmdb.org/t/p/w185" + castImage.file_path
                                    GlideImage(
                                        model = url,
                                        contentDescription = "Cast member image",
                                        Modifier.fillMaxWidth(.8F)
                                    )
                                } else Image(
                                    painter = painterResource(id = R.drawable.baseline_person),
                                    contentDescription = "Default cast member icon",
                                    Modifier.fillMaxWidth(.8F)
                                )
                                if (castMember.name != "") {
                                    Text(
                                        text = castMember.name,
                                        style = MaterialTheme.typography.body1
                                    )

                                }
                                if (castMember.character != "") {
                                    Text(
                                        text = castMember.character,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingPostersList(postersList: List<Image>) {
        if (postersList.isNotEmpty()) {
            Text("Posters", style = MaterialTheme.typography.h6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(postersList) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.file_path != "") {
                            val baseUrl = "https://image.tmdb.org/t/p/w185"
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
}


