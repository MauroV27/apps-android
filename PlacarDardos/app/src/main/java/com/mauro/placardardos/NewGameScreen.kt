package com.mauro.placardardos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.mauro.placardardos.data.Score

class NewGameScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game_screen)

        val namePlayerA : EditText = findViewById(R.id.etPlayerA)
        val namePlayerB : EditText = findViewById(R.id.etPlayerB)
        val initScoreValue : EditText = findViewById(R.id.editTextNumber)

        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        btnStartGame.setOnClickListener{

            val gameScore : Score = Score(
                namePlayerA.text.toString(),
                namePlayerB.text.toString(),
                initScoreValue.text.toString().toInt(),
                "None",
                initScoreValue.text.toString().toInt(),
                initScoreValue.text.toString().toInt()
            )

            val intentScoreScreen = Intent(this, ScoreScreen::class.java).apply{
                putExtra("score", gameScore)
            }
            startActivity(intentScoreScreen)
        }

    }
}