package com.android.tinysquare.presentation

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.tinysquare.presentation.util.toText
import com.google.android.gms.location.*
import com.android.tinysquare.R
import com.android.tinysquare.presentation.main.MainActivity
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * Service tracks location when requested and updates [VenuesFragment]. If [VenuesFragment]
 * is stopped/unbinds and tracking is enabled, the service promotes itself to a foreground service to
 * insure location updates aren't interrupted.
 *
 * For apps running in the background on O+ devices, location is computed much less than previous
 * versions.
 */
class ForegroundOnlyLocationService : Service() {

    private val notificationManager: NotificationManager by inject()
    private val locationRequest: LocationRequest by inject()
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var serviceRunningInForeground = false
    private val localBinder = LocalBinder()
    private var currentLocation: Location? = null

    override fun onCreate() {
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)

        locationRequest.apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.SECONDS.toMillis(10)
            smallestDisplacement = 100f //To avoid unnecessary updates when smallest displacement is under 100 meters
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.lastLocation != null) {
                    currentLocation = locationResult.lastLocation
                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST).apply {
                        putExtra(EXTRA_LOCATION, currentLocation)
                    }
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                    // Updates notification content if this service is running as a foreground service.
                    if (serviceRunningInForeground) {
                        notificationManager.notify(NOTIFICATION_ID, generateNotification(currentLocation))
                    }
                } else Log.d(TAG, "Location missing in callback.")

            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY // Tells the system not to recreate the service after it's been killed.
    }

    override fun onBind(intent: Intent): IBinder {
        // VenuesFragment comes into foreground and binds to service, so the service can become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        // VenuesFragment returns to the foreground and rebinds to service, so the service can become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        val notification = generateNotification(currentLocation)
        startForeground(NOTIFICATION_ID, notification)
        serviceRunningInForeground = true
        // Ensures onRebind() is called if VenuesFragment rebinds.
        return true
    }

    fun subscribeToLocationUpdates() {
        /* Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        * ensure this Service can be promoted to a foreground service.
        * */
        startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))

        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }

    private fun unsubscribeToLocationUpdates() {
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                    stopSelf()
                } else Log.d(TAG, "Failed to remove Location Callback.")
            }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }

    private fun generateNotification(location: Location?): Notification {
        val mainNotificationText = location?.toText() ?: getString(com.android.tinysquare.R.string.no_current_location)
        val titleText = getString(R.string.current_location)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)


        val launchActivityIntent = Intent(this, MainActivity::class.java)
        val cancelIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0)

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext,
            NOTIFICATION_CHANNEL_ID
        )
        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setSmallIcon(R.drawable.baseline_location_searching_black_24dp)
            .setContentText(mainNotificationText)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setDefaults(0)
            .setSound(null)
            .setVibrate(longArrayOf(-1L))
            .addAction(
                R.drawable.baseline_launch_black_24,
                getString(R.string.open),
                activityPendingIntent
            )
            .addAction(
                R.drawable.baseline_location_on_24,
                getString(R.string.stop_receiving_location_updates),
                servicePendingIntent
            )
            .build()
    }

    /**
     * Class used for the client Binder.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    companion object {
        private val TAG = ForegroundOnlyLocationService::class.java.name
        private const val PACKAGE_NAME = "com.android.tinysquare"
        private const val NOTIFICATION_ID = 100017
        private const val NOTIFICATION_CHANNEL_ID = "tinysquare_channel_01"
        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
    }
}