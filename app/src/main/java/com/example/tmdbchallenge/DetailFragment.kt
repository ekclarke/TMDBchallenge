package com.example.tmdbchallenge

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbchallenge.data.*
import com.example.tmdbchallenge.utilities.DateHelper
import com.example.tmdbchallenge.utilities.ImageHelper
import com.example.tmdbchallenge.utilities.OnlineHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class DetailFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(id: Int, repo: Repository, listener: OnClosedListener): DetailFragment {
            val detailFragment = DetailFragment()
            detailFragment.listener = listener
            detailFragment.viewModel = DetailViewModel(repo, id)
            return detailFragment
        }
    }

    private lateinit var movie: State<MovieDetails?>
    private lateinit var viewModel: DetailViewModel
    private lateinit var castList: State<List<Cast>>
    private lateinit var moviePosters: State<List<Image>>
    private lateinit var castImages: State<List<Image?>>
    private lateinit var listener: OnClosedListener
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            sharedPrefs = this.context.getSharedPreferences(
                this.context.getString(R.string.prefs_key),
                Context.MODE_PRIVATE
            )

            if(activity?.let {OnlineHelper.isOnline(it) } == true) {
                viewModel.refreshData(requireActivity())
                setContent {
                    MovieDetailScreen()
                }
            }
            else activity?.let { OnlineHelper.showOnlineDialog(it) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.onCancel()
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
                .verticalScroll(rememberScrollState(), enabled = true)
            //No native scrollbar support at this time unfortunately!
        ) {
            movie.value?.let { Header(it) }
            ScrollingCastList(castList.value, castImages.value)
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
            endX = 1500.0f
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val localDensity = LocalDensity.current
            var imageHeight by remember {
                mutableStateOf(0.dp)
            }
            var matchImage = false

            if (movie.backdrop_path != null && movie.backdrop_path != "") {
                val backdropUrl = context?.let { ImageHelper.getBackdropUrl(it, 3) }
                if (backdropUrl != "" && backdropUrl != null) {
                    matchImage = true
                    GlideImage(
                        model = backdropUrl + movie.backdrop_path,
                        contentDescription = "Movie backdrop",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                imageHeight = with(localDensity) { coordinates.size.height.toDp() }
                            })
                }
            } else Image(
                painter = painterResource(id = R.drawable.baseline_movie),
                contentDescription = "Default movie icon",
                Modifier.fillMaxWidth(1F)
            )
            Column(
                if (matchImage) Modifier
                    .background(gradient)
                    .fillMaxWidth()
                    .height(imageHeight)
                else Modifier
                    .background(gradient)
                    .fillMaxWidth()
            ) {
                if (movie.title != "") Text(
                    text = movie.title,
                    style = MaterialTheme.typography.h5,
                    color = colorResource(id = R.color.blue_200),
                    modifier = Modifier
                        .fillMaxWidth(.6F)
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
                if (movie.status.toString() != "")
                    Text(
                        text = movie.status.toString().lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.yellow_600),
                        modifier = Modifier
                            .fillMaxWidth(.6F)
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    )
                if (movie.release_date != "")
                    Text(
                        text = DateHelper.getFormattedDate(
                            movie.release_date,
                            LocalContext.current
                        ),
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .fillMaxWidth(.6F)
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    )
                if (movie.runtime != 0)
                    Text(
                        text = movie.runtime.toString() + " minutes",
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .fillMaxWidth(.6F)
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    )
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingCastList(castList: List<Cast>, imageList: List<Image?>) {
        if (imageList.isEmpty() && castList.isNotEmpty()) {
            Text("Cast", style = MaterialTheme.typography.h6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                items(castList) { castMember ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_person),
                            contentDescription = "Default cast member icon"
                        )
                        if (castMember.name != "") {
                            Text(
                                text = castMember.name,
                                style = MaterialTheme.typography.body2
                            )

                        }
                        if (castMember.character != "") {
                            Text(
                                text = castMember.character,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        } else if (imageList.isNotEmpty() && castList.isNotEmpty()) {
            Text("Cast", style = MaterialTheme.typography.h6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            {
                items(castList) { castMember ->
                    val castImage = castImages.value[castMember.order]
                    if (castImage != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (castImage.file_path != "") {
                                val profileUrl =
                                    context?.let { ImageHelper.getProfileUrl(it, 3) }
                                if (profileUrl != "" && profileUrl != null)
                                    GlideImage(
                                        model = profileUrl + castImage.file_path,
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

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ScrollingPostersList(postersList: List<Image>) {
        if (postersList.isNotEmpty()) {
            Text("Posters", style = MaterialTheme.typography.h6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)

            ) {
                items(postersList) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.file_path != "") {
                            val posterUrl =
                                context?.let { ImageHelper.getPosterUrl(it, 1) }
                            if (posterUrl != "" && posterUrl != null)
                                GlideImage(
                                    model = posterUrl + item.file_path,
                                    contentDescription = "Movie poster"
                                )
                        } else Image(
                            painter = painterResource(id = R.drawable.baseline_poster),
                            contentDescription = "Default poster icon"
                        )
                    }
                }
            }
        }
    }
}

interface OnClosedListener {
    fun onCancel()
    fun onDismiss()
}


