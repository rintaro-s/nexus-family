package com.lorinta.nexus_family

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val agreeButton = findViewById<Button>(R.id.agreeButton)
        agreeButton.setOnClickListener {
            // 同意フラグを保存
            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("hasAgreed", true)
                apply()
            }

            // MainActivityへ遷移
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
