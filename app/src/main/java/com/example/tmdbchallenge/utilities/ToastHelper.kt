package com.example.tmdbchallenge.utilities

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.tmdbchallenge.R

object ToastHelper {

    fun showErrorToast(activity: Activity, specificData: String) {
        Toast.makeText(
            activity,
            "Error retrieving " + specificData,
            Toast.LENGTH_SHORT
        ).show()
    }
}