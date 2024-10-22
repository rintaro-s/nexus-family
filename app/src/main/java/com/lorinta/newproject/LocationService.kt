package com.lorinta.newproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import okhttp3.*
import java.io.IOException

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val targetLocation = Location("provider").apply {
        latitude = 34.4793121 // ここがざひょう (e.g., Tokyo Station)
        longitude = 136.8186805
    }
    private val targetRadius = 500 // 500 meters
    private val lineNotifyToken = "VqaiWrOso0aMtn2bnjEnIkLmMrLWEfhaq7p4jugSIWd" // Set your LINE Notify token here

    override fun onCreate() {
        super.onCreate()

        try {
            // Initialize location client and request
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationRequest = LocationRequest.create().apply {
                interval = 10000 // 10 seconds
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation ?: return
                    handleLocationUpdate(location)
                }
            }

            // Start location updates
            startLocationUpdates()

            // Start foreground notification
            startForeground(1, createNotification())
        } catch (e: Exception) {
            sendErrorViaLineNotify("Location service error: ${e.message}")
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "LocationServiceChannel"
        val channel = NotificationChannel(
            notificationChannelId,
            "Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Location Tracking")
            .setContentText("Tracking location in the background")
            .setSmallIcon(android.R.drawable.ic_dialog_map) // Use built-in drawable
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun handleLocationUpdate(location: Location) {
        val distance = location.distanceTo(targetLocation)
        if (distance <= targetRadius) {
            sendNotification("近づきました", distance)
        }
    }

    private fun sendNotification(message: String, distance: Float) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("message", "$message: 距離 $distance メートルです。")
            .build()
        val request = Request.Builder()
            .url("https://notify-api.line.me/api/notify")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $lineNotifyToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                sendErrorViaLineNotify("Failed to send notification via LINE Notify: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                // Do nothing, message sent successfully
            }
        })
    }

    private fun sendErrorViaLineNotify(error: String) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("message", "Error: $error")
            .build()
        val request = Request.Builder()
            .url("https://notify-api.line.me/api/notify")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $lineNotifyToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                // Do nothing, error sent successfully
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
