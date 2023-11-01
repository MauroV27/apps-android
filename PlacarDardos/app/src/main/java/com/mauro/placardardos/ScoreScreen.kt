package com.mauro.placardardos

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.mauro.placardardos.data.Score

class ScoreScreen : AppCompatActivity() {
    lateinit var score: Score

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_screen)

//        score = getIntent().getExtras()?.getSerializable("score") as Score
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            score = intent.getSerializableExtra("score", Score::class.java)!!
        } else {
            score = getIntent().getExtras()?.getSerializable("score") as Score
        }

        val tvNamePLayerA = findViewById<TextView>(R.id.tvNamePlayerA)
        val tvNamePLayerB = findViewById<TextView>(R.id.tvNamePlayerB)
        val tvInitScore = findViewById<TextView>(R.id.tvInitScore)

        tvNamePLayerA.text = score.playerA.toString()
        tvNamePLayerB.text = score.playerB.toString()
        tvInitScore.text = score.initScores.toString()

    }
}