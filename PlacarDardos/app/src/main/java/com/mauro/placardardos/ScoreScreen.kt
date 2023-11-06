package com.mauro.placardardos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.mauro.placardardos.data.Score
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets
import java.util.Date

class ScoreScreen : AppCompatActivity() {
    lateinit var score: Score

    private var playerAturn : Boolean = true
    private var historyPlays = mutableListOf<Int>()
    private var numPlaysAPlayer : Int = 0
    private var numPlaysBPlayer : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_screen)

//        score = getIntent().getExtras()?.getSerializable("score") as Score
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            score = intent.getSerializableExtra("score", Score::class.java)!!
        } else {
            score = getIntent().getExtras()?.getSerializable("score") as Score
        }

        Log.d("PDM23", "SCore: $score")

        // Load player names :
        val tvNamePlayerA = findViewById<TextView>(R.id.tvNamePlayerA)
        tvNamePlayerA.text = score.playerA.toString()

        val tvNamePlayerB = findViewById<TextView>(R.id.tvNamePlayerB)
        tvNamePlayerB.text = score.playerB.toString()

        this.updateScoresLayout()

        // Image buttons in screen :
        val icBtnBackHome = findViewById<ImageButton>(R.id.imgBtnBackHome)
        icBtnBackHome.setOnClickListener{
            val intentGoHome = Intent(this, MainActivity::class.java).apply{}
            startActivity(intentGoHome)
        }

        val icBtnTrash = findViewById<ImageButton>(R.id.imgBtnTrash)
        icBtnBackHome.setOnClickListener{
            // ClearCurrentGameSharedPreference

            // Back to home :
            val intentGoHome = Intent(this, MainActivity::class.java).apply{}
            startActivity(intentGoHome)
        }

        // Buttons in screen :
        val btnAddScore : Button = findViewById(R.id.btnAddScore)
        btnAddScore.setOnClickListener{
            val etScore : EditText = findViewById(R.id.etScoreInputValue)
            val value : Int = etScore.text.toString().toInt()

            // Forma como o historico é estruturado ( tudo em uma única pilha ) ele sabe quem é por ter a cada 3 jogadas
            // (0 1 2) (3 4 5) (6 7 8)
            this.playerAturn = ( this.historyPlays.size / 3 ) % 2 == 0

            this.historyPlays.add(value)
            updateScorePoints(value, -1)

            this.updateScoresLayout()
        }

        val btnBackScore : Button = findViewById(R.id.btnBackScore)
        btnBackScore.setOnClickListener{
            val histSize : Int  = this.historyPlays.size - 1

            if ( histSize < 0 ){
                return@setOnClickListener
            }

            val value = this.historyPlays.get(histSize)
            this.historyPlays.removeAt(histSize)

            this.playerAturn = ( (histSize ) / 3 ) % 2 == 0
            updateScorePoints(value, 1)

            this.updateScoresLayout()
        }

        val btnSaveGame : Button = findViewById(R.id.btnSaveGame)
        btnSaveGame.setOnClickListener{
            // ClearCurrentGameSharedPreference

            if ( this.score.finalScorePlayerA < this.score.finalScorePlayerB ){
                this.score.winner = this.score.playerA
                this.score.finalDistanceBtwPlayers = this.score.finalScorePlayerB - this.score.finalScorePlayerA
            } else {
                this.score.winner = this.score.playerB
                this.score.finalDistanceBtwPlayers = this.score.finalScorePlayerA - this.score.finalScorePlayerB
            }

            this.saveGame()

            // Move to hystory screen :
            val intentGoHistory = Intent(this, HistoryScreen::class.java).apply{}
            startActivity(intentGoHistory)
        }

    }

    private fun updateScorePoints(value:Int, sing:Int){
        if ( this.playerAturn ){
            this.score.finalScorePlayerA = this.score.finalScorePlayerA + ( value * sing )
            this.numPlaysAPlayer = this.numPlaysAPlayer - sing
        } else {
            this.score.finalScorePlayerB = this.score.finalScorePlayerB + ( value * sing )
            this.numPlaysBPlayer = this.numPlaysBPlayer - sing
        }
    }

    private fun updateScoresLayout() {

        // Player A element to acess

        val tvPlayerAScores = findViewById<TextView>(R.id.tvPlayerAScores)
        val tvPlayerATurnCounter = findViewById<TextView>( R.id.tvPlayerATurnNumber )
        val tvPlayerALastPoints = findViewById<TextView>(R.id.tvPlayerALastPoints)

        // Player B element to acess

        val tvPlayerBScores = findViewById<TextView>(R.id.tvPlayerBScores)
        val tvPlayerBTurnCounter = findViewById<TextView>( R.id.tvPlayerBTurnNumber )
        val tvPlayerBLastPoints = findViewById<TextView>(R.id.tvPlayerBLastPoints)

        // Show player turn :
        val tvPlayerTurn = findViewById<TextView>(R.id.tvTurnPlayerLabel)
        if ( this.playerAturn ){
            tvPlayerTurn.text = getString(R.string.txt_label_plauer_turn, this.score.playerA)
        } else {
            tvPlayerTurn.text = getString(R.string.txt_label_plauer_turn, this.score.playerB)
        }

        tvPlayerAScores.text = score.finalScorePlayerA.toString()
        tvPlayerATurnCounter.text = numPlaysAPlayer.toString()
        tvPlayerALastPoints.text = ""


        tvPlayerBScores.text = score.finalScorePlayerB.toString()
        tvPlayerBTurnCounter.text = numPlaysBPlayer.toString()
        tvPlayerBLastPoints.text = ""

        if ( this.score.finalScorePlayerA <= 0 || this.score.finalScorePlayerB <= 0 ){
            val btnSaveGame = findViewById<Button>(R.id.btnSaveGame)
            btnSaveGame.visibility = View.VISIBLE
        }
    }

    fun saveGame() {

        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()
        //Salvar o número de jogos já armazenados
        var numMatches= sp.getInt("numberMatch",0) + 1
        edShared.putInt("numberMatch", numMatches)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(this.score);

        //Salvar como "match"
        edShared.putString("match"+numMatches, dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit() //Não esqueçam de comitar!!!

    }
}