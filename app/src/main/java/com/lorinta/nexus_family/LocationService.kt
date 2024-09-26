package com.lorinta.nexus_family

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class LocationService : Service() {
    private var targetLatitude: Double = 0.0
    private var targetLongitude: Double = 0.0
    private var emailSent = false // To track whether the email has been sent
    private var handler: Handler? = null
    private var emailCooldown: Boolean = false // To manage the 10-minute cooldown

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        targetLatitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
        targetLongitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0

        handler = Handler(Looper.getMainLooper())
        monitorLocation()

        return START_STICKY
    }

    private fun monitorLocation() {
        handler?.post(object : Runnable {
            override fun run() {
                checkLocation()
                handler?.postDelayed(this, 5000) // Check every 5 seconds
            }
        })
    }

    private fun checkLocation() {
        // Replace with actual location checking code
        val currentLocation = Location("").apply {
            latitude = 35.689472
            longitude = 139.691750
        }

        val distance = FloatArray(1)
        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, targetLatitude, targetLongitude, distance)

        if (distance[0] < 500 && !emailSent && !emailCooldown) {
            // Send email and start 10-minute cooldown
            sendEmail()
            emailSent = true
            startCooldown()
        } else if (distance[0] >= 500) {
            // Reset if user leaves the 500m radius
            emailSent = false
            emailCooldown = false // Reset cooldown if they leave the radius
        }
    }

    private fun startCooldown() {
        // Start a 10-minute cooldown
        emailCooldown = true
        handler?.postDelayed({
            emailCooldown = false // After 10 minutes, allow email sending again
        }, 10 * 60 * 1000) // 10 minutes in milliseconds
    }

    private fun sendEmail() {
        val username = "by-your@gmail.com"
        val password = "by-YourPass"

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse("to-your@gmail.com"))
                subject = "警告！"
                setText("このメールは利用規約に同意し、自動送信されたものであり開発者は一切の責任を負いません。")
            }

            Transport.send(message)
            Log.d("LocationService", "Email sent successfully")
        } catch (e: MessagingException) {
            Log.e("LocationService", "Error sending email", e)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
