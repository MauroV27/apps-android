package com.mauro.placardardos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.mauro.placardardos.data.Score
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Go to New Game screen
        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener {

            val sharedPreferences = getSharedPreferences("GameScorerStatus", Context.MODE_PRIVATE)
            val existSharedPref = sharedPreferences.getString("score_status", null)

            Log.d("PDM23", existSharedPref.toString())

            if ( existSharedPref != null ){

                val bis: ByteArrayInputStream = ByteArrayInputStream(existSharedPref.toByteArray(Charsets.ISO_8859_1))
                val obi: ObjectInputStream = ObjectInputStream(bis)

                var placar: Score = obi.readObject() as Score

                val intentPreviosGame = Intent(this, ScoreScreen::class.java).apply {
                    putExtra("score", placar)
                    putExtra("numPlaysByTurn", sharedPreferences.getInt("numPlaysByTurn", 3))
                }

                startActivity((intentPreviosGame))
            } else {
                val intentNewGame = Intent(this, NewGameScreen::class.java).apply {}
                startActivity(intentNewGame)
            }
        }

        // Go to History screen
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        btnHistory.setOnClickListener {
            val intentHistory = Intent(this, HistoryScreen::class.java).apply{}
            startActivity(intentHistory)
        }

        val icBtnTrash = findViewById<ImageButton>(R.id.imgBtnTrash)
        icBtnTrash.setOnClickListener{
            val sharedPreferences = getSharedPreferences("GameScorerStatus", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()

            // Remove o dado associado à chave
            editor.remove("score_status")
            editor.remove("darts_per_round")

            editor.apply()
        }

        // Go to Rules screen
        val btnRules = findViewById<Button>(R.id.btnRules)
        btnRules.setOnClickListener {
            val intentRules = Intent(this, RulesScreen::class.java).apply{}
            startActivity(intentRules)
        }
    }
}