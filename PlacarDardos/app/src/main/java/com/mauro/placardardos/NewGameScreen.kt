package com.mauro.placardardos

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.mauro.placardardos.data.Score
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

class NewGameScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game_screen)

        val namePlayerA : EditText = findViewById(R.id.etPlayerA)
        val namePlayerB : EditText = findViewById(R.id.etPlayerB)
        val initScoreValue : EditText = findViewById(R.id.editTextNumber)

        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        btnStartGame.setOnClickListener{

            val formatter = SimpleDateFormat(getString(R.string.date_format_code))
            val date = Date()
            val currentDate = formatter.format(date)

            val gameScore : Score = Score(
                namePlayerA.text.toString(),
                namePlayerB.text.toString(),
                initScoreValue.text.toString().toInt(),
                "None",
                initScoreValue.text.toString().toInt(),
                initScoreValue.text.toString().toInt(),
                12,
                currentDate
            )

            val intentScoreScreen = Intent(this, ScoreScreen::class.java).apply{
                putExtra("score", gameScore)
            }
            startActivity(intentScoreScreen)
        }

    }
}