package com.lorinta.nexus_family

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.SeekBar
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var timerSeekBar: SeekBar
    private lateinit var startTimerButton: Button
    private var timer: CountDownTimer? = null
    private var timerSetInSeconds: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        timerSeekBar = findViewById(R.id.timerSeekBar)
        startTimerButton = findViewById(R.id.startTimerButton)

        // SeekBar でタイマーを設定
        timerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minutes = progress / 60
                val seconds = progress % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
                timerSetInSeconds = progress.toLong()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // スタートボタンを押したとき
        startTimerButton.setOnClickListener {
            if (timerSetInSeconds == 631L) { // 10:31の時はデバッグモード
                val intent = Intent(this, DebugActivity::class.java)
                startActivity(intent)
            } else {
                startTimer(timerSetInSeconds)
            }
        }
    }

    private fun startTimer(seconds: Long) {
        timer?.cancel()  // 前のタイマーがあればキャンセル
        timer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
            }
        }.start()
    }
}

