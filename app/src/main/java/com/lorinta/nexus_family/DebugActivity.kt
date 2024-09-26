

package com.lorinta.nexus_family

import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DebugActivity : AppCompatActivity() {

    private lateinit var latEditText: EditText
    private lateinit var lonEditText: EditText
    private lateinit var debugInfoTextView: TextView
    private lateinit var requestPermissionsButton: Button
    private lateinit var distanceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        latEditText = findViewById(R.id.latEditText)
        lonEditText = findViewById(R.id.lonEditText)
        debugInfoTextView = findViewById(R.id.debugInfoTextView)
        requestPermissionsButton = findViewById(R.id.requestPermissionsButton)
        distanceTextView = findViewById(R.id.distanceTextView)

        // 緯度・経度を指定して計算
        requestPermissionsButton.setOnClickListener {
            val lat = latEditText.text.toString().toDoubleOrNull() ?: 0.0
            val lon = lonEditText.text.toString().toDoubleOrNull() ?: 0.0
            calculateDistance(lat, lon)
        }
    }

    private fun calculateDistance(lat: Double, lon: Double) {
        // 仮に自分の現在地の座標を固定で設定
        val currentLat = 35.689472
        val currentLon = 139.691750
        val distance = FloatArray(1)

        Location.distanceBetween(currentLat, currentLon, lat, lon, distance)
        distanceTextView.text = "Distance to target: ${distance[0].toInt()} meters"
    }
}
