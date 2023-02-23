package com.example.tmdbchallenge.utilities

import android.content.Context
import android.widget.ImageView
import com.example.tmdbchallenge.R
import com.squareup.picasso.Picasso

object ImageHelper {

    private fun getBaseUrl(context: Context): String? {
        return context.getSharedPreferences(
            context.getString(R.string.prefs_key),
            Context.MODE_PRIVATE
        ).getString(
            context.getString(R.string.secure_base_url_key), null
        )
    }

    fun getPosterUrl(context: Context, option: Int): String? {
       val posterSizeList =
           context.getSharedPreferences(
               context.getString(R.string.prefs_key),
               Context.MODE_PRIVATE
           ).getStringSet(
                context.getString(R.string.poster_sizes_key),
                mutableSetOf()
            )
        if(posterSizeList?.isEmpty() == true) return null
        val posterSize = posterSizeList?.elementAt(option)
        return getBaseUrl(context) + "/" + posterSize
    }

    fun getProfileUrl(context: Context, option: Int): String? {
        val profileSizeList =
            context.getSharedPreferences(
                context.getString(R.string.prefs_key),
                Context.MODE_PRIVATE
            ).getStringSet(
                context.getString(R.string.profile_sizes_key),
                mutableSetOf()
            )
        if(profileSizeList?.isEmpty() == true) return null
        val profileSize = profileSizeList?.elementAt(option)
        return getBaseUrl(context) + "/" + profileSize
    }

    fun getBackdropUrl(context: Context, option: Int): String? {
        val backdropSizeList =
            context.getSharedPreferences(
                context.getString(R.string.prefs_key),
                Context.MODE_PRIVATE
            ).getStringSet(
                context.getString(R.string.backdrop_sizes_key),
                mutableSetOf()
            )
        if(backdropSizeList?.isEmpty() == true) return null
        val backdropSize = backdropSizeList?.elementAt(option)
        return getBaseUrl(context) + "/" + backdropSize
    }

    fun loadImage(url: String, view: ImageView) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.baseline_movie)
            .into(view)
    }
}