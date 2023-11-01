package com.mauro.placardardos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Go to New Game screen
        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener {
            val intentNewGame = Intent(this, NewGameScreen::class.java).apply{}
            startActivity(intentNewGame)
        }

        // Go to History screen
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        btnHistory.setOnClickListener {
            val intentHistory = Intent(this, HistoryScreen::class.java).apply{}
            startActivity(intentHistory)
        }

        // Go to Rules screen
        val btnRules = findViewById<Button>(R.id.btnRules)
        btnRules.setOnClickListener {
            val intentRules = Intent(this, RulesScreen::class.java).apply{}
            startActivity(intentRules)
        }
    }
}