package com.android.tinysquare.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.tinysquare.presentation.util.isNetworkAvailable

class NetworkStateBroadcastReceiver : BroadcastReceiver(){

    private var listeners: MutableSet<OnNetworkStateReceiverListener> = HashSet()
    private var connected: Boolean? = null

    override fun onReceive(context: Context, intent: Intent) {
        connected = isNetworkAvailable(context)
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        for (listener in listeners)
            notifyState(listener)
    }

    private fun notifyState(listener: OnNetworkStateReceiverListener?) {
        if (connected == true) listener?.networkAvailable()
        else listener?.networkUnavailable()
    }

    fun addListener(listener: OnNetworkStateReceiverListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: OnNetworkStateReceiverListener) {
        listeners.remove(listener)
    }

    interface OnNetworkStateReceiverListener {
        fun networkAvailable()
        fun networkUnavailable()
    }
}
