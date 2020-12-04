package com.android.tinysquare.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.tinysquare.util.isNetworkAvailable


class NetworkStateBroadcastReceiver : BroadcastReceiver(){

    private var listeners: MutableSet<OnNetworkStateReceiverListener> = HashSet()
    private var connected: Boolean? = null

    override fun onReceive(context: Context, intent: Intent) {
        connected = isNetworkAvailable(context)
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        Log.d(TAG, "notifyStateToAll() called")
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


    companion object{

        private val TAG = NetworkStateBroadcastReceiver::class.java.name
    }

}
