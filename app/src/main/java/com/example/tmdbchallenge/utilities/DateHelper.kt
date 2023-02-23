package com.example.tmdbchallenge.utilities

import android.content.Context
import android.icu.text.SimpleDateFormat
import java.util.*


object DateHelper {

    private fun formatToDate(dateString: String, context: Context): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd", context.resources.configuration.locales.get(0))
        return formatter.parse(dateString)
    }

    fun getReleaseYear(dateString: String, context: Context): String{
        val splitDate = dateString.split("-")
        return splitDate[0]
    }

    fun getFormattedDate(dateString: String, context: Context): String {
        val fullDate = formatToDate(dateString, context)
        val formatter = SimpleDateFormat("MMMM dd, yyyy", context.resources.configuration.locales.get(0))
        return formatter.format(fullDate)
    }

}