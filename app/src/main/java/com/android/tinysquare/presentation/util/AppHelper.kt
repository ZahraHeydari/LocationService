package com.android.tinysquare.presentation.util


import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


inline fun <reified T : Fragment> newFragmentInstance(vararg params: Pair<String, Any?>): T {
    return T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }
}

/**
 * Returns the location object as a readable string.
 */
fun Location?.toText() = if (this != null) "$latitude, $longitude" else "Unknown location"


fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    //Internet connectivity check in Android Q
    val networks = connectivityManager.allNetworks
    var hasInternet = false
    if (networks.isNotEmpty()) {
        for (network in networks) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) hasInternet = true
        }
    }
    return hasInternet
}