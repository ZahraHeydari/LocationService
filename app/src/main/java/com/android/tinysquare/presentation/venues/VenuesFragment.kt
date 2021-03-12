package com.android.tinysquare.presentation.venues

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.tinysquare.BuildConfig
import com.android.tinysquare.R
import com.android.tinysquare.databinding.FragmentVenuesBinding
import com.android.tinysquare.domain.model.ApiError
import com.android.tinysquare.domain.model.Venue
import com.android.tinysquare.presentation.ForegroundOnlyLocationService
import com.android.tinysquare.presentation.NetworkStateBroadcastReceiver
import com.android.tinysquare.presentation.main.OnMainActivityCallback
import com.android.tinysquare.presentation.util.PageEndlessScrollController
import com.android.tinysquare.presentation.util.isNetworkAvailable
import com.android.tinysquare.presentation.util.toText
import com.android.tinysquare.presentation.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_venues.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope


class VenuesFragment : Fragment(), PageEndlessScrollController.OnLoadMoreScrollListener,
NetworkStateBroadcastReceiver.OnNetworkStateReceiverListener{


    private var _binding: FragmentVenuesBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.
    private var foregroundOnlyLocationServiceBound = false
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
    private val venuesViewModel: VenuesViewModel by inject()
    private val networkStateBroadcastReceiver : NetworkStateBroadcastReceiver by currentScope.inject()
    private var location: Location ?= null
    private var callback: OnMainActivityCallback? = null


    private val foregroundOnlyBroadcastReceiver by lazy {
        ForegroundOnlyBroadcastReceiver()
    }

    private val adapter: VenuesAdapter by lazy {
        VenuesAdapter(object : VenuesAdapter.OnVenuesItemOnClickListener {

            override fun onItemClick(venue: Venue) {
                callback?.navigateToDetail(venue)
            }
        })
    }

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true

            if (foregroundPermissionApproved()) foregroundOnlyLocationService?.subscribeToLocationUpdates()
                ?: Log.d(TAG, "Service Not Bound")
            else requestForegroundPermissions()

        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkStateBroadcastReceiver.addListener(this)
        context?.registerReceiver(networkStateBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMainActivityCallback) {
            callback = context
        } else throw ClassCastException("$context must implement OnMainActivityCallback!")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVenuesBinding.inflate(inflater, container, false)
        binding.venuesAppBarLayout.venues_toolbar.title = context?.getString(R.string.places_around_me)

        if (!adapter.hasObservers()) adapter.setHasStableIds(true)//a unique value as a key for item & try to use the same viewholder and view for the same id.
        binding.venuesRecyclerView.adapter = adapter
        binding.venuesRecyclerView.addOnScrollListener(PageEndlessScrollController(this))


        with(venuesViewModel) {
            venues.observe(viewLifecycleOwner, Observer {
                adapter.venueList = it
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                binding.venuesProgressBar.visibility = if (it) View.VISIBLE else View.GONE
            })

            networkError.observe(viewLifecycleOwner, Observer { error ->
                onErrorHappened(error)
            })
        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(context, ForegroundOnlyLocationService::class.java)
        context?.bindService(
            serviceIntent,
            foregroundOnlyServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()

        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                foregroundOnlyBroadcastReceiver,
                IntentFilter(ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
            )
        }
    }

    override fun onPause() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(foregroundOnlyBroadcastReceiver)
        } //To avoid memory leak

        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            context?.unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    // Checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == context?.let {
            ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide additional rationale.
        if (provideRationale) {
            Snackbar.make(binding.root, R.string.permission_rationale, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok) {
                    // Request permission
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    ) }.show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    // Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.
                    Snackbar.make(
                        binding.root,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                data = uri
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        }.show()
                }
            }
        }
    }

    private fun updateUI(location: Location?) {
        Log.d(TAG, "updateUI() called with: location = $location")

        if (location != null) {
            with(venuesViewModel) {
                checkLocation(location.latitude.toString(),location.longitude.toString())

                viewModelScope.launch {
                    delay(100)
                    clearFormerData()

                    /*
                     * Fetches data (venues) from network if internet is available or location is new
                     * Otherwise local data will be displayed
                     * */
                    if (isNetworkAvailable(requireContext()) && isNewLocation()) fetchVenues(location.toText())
                    else fetchVenuesFromDb()
                }
            }
        }
    }

    private fun onErrorHappened(apiError: ApiError) {
        context?.toast(apiError.getErrorMessage())
    }

    override fun onLoadMore() {
        context?.let {
            if(isNetworkAvailable(it)) venuesViewModel.loadMoreItems()
        }
    }

    override fun getTotalCount(): Int = venuesViewModel.getTotalCount()

    override fun isLoading(): Boolean = venuesViewModel.getLoadingMore()


    override fun networkAvailable() {
        if(location == null) return
        Log.d(TAG, "networkAvailable() called")
        with(venuesViewModel){
            if(isNewLocation())
                clearFormerData()
                fetchVenues(location.toText())
        }
    }

    override fun networkUnavailable() {
        Log.d(TAG, "networkUnavailable() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //To avoid memory leak
    }

    override fun onDestroy() {
        networkStateBroadcastReceiver.removeListener(this)
        context?.unregisterReceiver(networkStateBroadcastReceiver) //To avoid memory leak
        super.onDestroy()
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }


    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            location = intent.getParcelableExtra(ForegroundOnlyLocationService.EXTRA_LOCATION)
            updateUI(location)
        }
    }


    companion object {

        private val TAG = VenuesFragment::class.java.name
        val FRAGMENT_NAME: String = VenuesFragment::class.java.simpleName
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1034
    }
}