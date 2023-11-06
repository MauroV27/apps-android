package com.mauro.placardardos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class RulesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules_screen)

        // Back to home screen
        val icBtnBackHome = findViewById<ImageButton>(R.id.imgBtnBackHome)
        icBtnBackHome.setOnClickListener{
            val intentGoHome = Intent(this, MainActivity::class.java).apply{}
            startActivity(intentGoHome)
        }
    }
}