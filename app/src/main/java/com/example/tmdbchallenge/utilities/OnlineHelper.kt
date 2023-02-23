package com.example.tmdbchallenge.utilities

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings.ACTION_SETTINGS
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

object OnlineHelper {

    fun isOnline(activity: Activity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    fun showOnlineDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Oops! It looks like you're offline. Please turn wifi or data on and try again.")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                startActivity(activity, Intent(ACTION_SETTINGS), null)
                activity.finishAndRemoveTask()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}