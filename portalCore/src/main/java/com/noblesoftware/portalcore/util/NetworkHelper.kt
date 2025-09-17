package com.noblesoftware.portalcore.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject

class NetworkHelper @Inject constructor(private val context: Context) {

    private val TAG = NetworkHelper::class.simpleName!!

    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = cm.activeNetwork ?: return false
            val actNw = cm.getNetworkCapabilities(nw) ?: return false
            val ret = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
            return ret
        } else {
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected == true
        }
    }

    fun getString(stringId: Int): String =
        kotlin.runCatching {
            context.getString(stringId)
        }.getOrElse { "" }
}