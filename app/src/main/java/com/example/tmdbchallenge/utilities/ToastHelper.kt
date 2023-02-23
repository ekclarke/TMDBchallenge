package com.example.tmdbchallenge.utilities

import android.content.Context
import android.widget.Toast
import com.example.tmdbchallenge.R

object ToastHelper {

    fun showErrorToast(context: Context, specificData: String) {
        Toast.makeText(
            context,
            "Error retrieving " + specificData,
            Toast.LENGTH_SHORT
        ).show()
    }
}