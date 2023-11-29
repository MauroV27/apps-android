package com.mauro.placardardos

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mauro.placardardos.data.Score
import com.mauro.placardardos.data.ScoreDAO
import com.mauro.placardardos.data.ScoreRoomDB
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class ScoreScreen : AppCompatActivity() {
    lateinit var score: Score

//    private lateinit var dao: ScoreDAO

    private var playerAturn: Boolean = true
    private var gameEnd: Boolean = false
    private var historyPlays = mutableListOf<Int>()
    private var numPlaysAPlayer: Int = 0
    private var numPlaysBPlayer: Int = 0

    private var numPlaysByTurn: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_screen)

//        this.dao = ScoreRoomDB.getInstance(this).scoreDAO()

//        score = getIntent().getExtras()?.getSerializable("score") as Score
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            score = intent.getSerializableExtra("score", Score::class.java)!!
            numPlaysByTurn = intent.getIntExtra("numPlaysByTurn", 3)
        } else {
            score = getIntent().getExtras()?.getSerializable("score") as Score
        }

        // Load list of scores from SharedPreference :
        val shPrefs = getSharedPreferences("GameScorerStatus", Context.MODE_PRIVATE)
        this.numPlaysAPlayer = shPrefs.getString("playerA_plays", "0")?.toIntOrNull()!!
        this.numPlaysBPlayer = shPrefs.getString("playerB_plays", "0")?.toIntOrNull()!!

        // Load player names :
        val tvNamePlayerA = findViewById<TextView>(R.id.tvNamePlayerA)
        tvNamePlayerA.text = score.playerA.toString()

        val tvNamePlayerB = findViewById<TextView>(R.id.tvNamePlayerB)
        tvNamePlayerB.text = score.playerB.toString()

        this.updateScoresLayout()

        // Image buttons in screen :
        val icBtnBackHome = findViewById<ImageButton>(R.id.imgBtnBackHome)
        icBtnBackHome.setOnClickListener {
            val intentGoHome = Intent(this, MainActivity::class.java).apply {}
            startActivity(intentGoHome)
        }

        val icBtnTrash = findViewById<ImageButton>(R.id.imgBtnTrash)
        icBtnTrash.setOnClickListener {
            this.clearCurrentGameSharedPreference()

            // Back to home :
            val intentGoHome = Intent(this, MainActivity::class.java).apply {}
            startActivity(intentGoHome)
        }

        // Buttons in screen :
        val btnAddScore: Button = findViewById(R.id.btnAddScore)
        btnAddScore.setOnClickListener {

            if (this.gameEnd) {
                return@setOnClickListener
            }

            val etScore: EditText = findViewById(R.id.etScoreInputValue)
            val value: Int = etScore.text.toString().toInt()

            // Forma como o historico é estruturado ( tudo em uma única pilha ) ele sabe quem é por ter a cada 3 jogadas
            // (0 1 2) (3 4 5) (6 7 8)
            this.playerAturn = (this.historyPlays.size / this.numPlaysByTurn) % 2 == 0

            this.historyPlays.add(value)
            updateScorePoints(value, -1)

            this.updateScoresLayout()
            this.updateCurrentGameSharedPreference()
        }

        val btnBackScore: Button = findViewById(R.id.btnBackScore)
        btnBackScore.setOnClickListener {
            val histSize: Int = this.historyPlays.size - 1

            if (histSize < 0) {
                return@setOnClickListener
            }

            val value = this.historyPlays.get(histSize)
            this.historyPlays.removeAt(histSize)

            this.playerAturn = ((histSize) / this.numPlaysByTurn) % 2 == 0
            updateScorePoints(value, 1)

            this.updateScoresLayout()
            this.updateCurrentGameSharedPreference()
        }

        val btnSaveGame: Button = findViewById(R.id.btnSaveGame)
        btnSaveGame.setOnClickListener {

            if (this.score.finalScorePlayerA < this.score.finalScorePlayerB) {
                this.score.winner = this.score.playerA
                this.score.finalDistanceBtwPlayers =
                    this.score.finalScorePlayerB - this.score.finalScorePlayerA
            } else {
                this.score.winner = this.score.playerB
                this.score.finalDistanceBtwPlayers =
                    this.score.finalScorePlayerA - this.score.finalScorePlayerB
            }

            this.clearCurrentGameSharedPreference()
            val db = Room.databaseBuilder(
                this,
                ScoreRoomDB::class.java, ScoreRoomDB.DB_NAME
            ).build()

            val dao = db.scoreDAO()
            this.saveGame(dao, this.score)


            // Move to hystory screen :
            val intentGoHistory = Intent(this, HistoryScreen::class.java).apply {}
            startActivity(intentGoHistory)
        }

    }

    private fun updateScorePoints(value: Int, sing: Int) {
        if (this.playerAturn) {
            this.score.finalScorePlayerA = this.score.finalScorePlayerA + (value * sing)
            this.numPlaysAPlayer = this.numPlaysAPlayer - sing
        } else {
            this.score.finalScorePlayerB = this.score.finalScorePlayerB + (value * sing)
            this.numPlaysBPlayer = this.numPlaysBPlayer - sing
        }
    }

    private fun updateScoresLayout() {

        // Player A element to acess

        val tvPlayerAScores = findViewById<TextView>(R.id.tvPlayerAScores)
        val tvPlayerATurnCounter = findViewById<TextView>(R.id.tvPlayerATurnNumber)
        val tvPlayerALastPoints = findViewById<TextView>(R.id.tvPlayerALastPoints)

        // Player B element to acess

        val tvPlayerBScores = findViewById<TextView>(R.id.tvPlayerBScores)
        val tvPlayerBTurnCounter = findViewById<TextView>(R.id.tvPlayerBTurnNumber)
        val tvPlayerBLastPoints = findViewById<TextView>(R.id.tvPlayerBLastPoints)

        // Show player turn :
        val tvPlayerTurn = findViewById<TextView>(R.id.tvTurnPlayerLabel)
        if (this.playerAturn) {
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

        val btnSaveGame = findViewById<Button>(R.id.btnSaveGame)

        if (this.score.finalScorePlayerA <= 0 || this.score.finalScorePlayerB <= 0) {
            btnSaveGame.visibility = View.VISIBLE
            this.gameEnd = true
        } else {
            btnSaveGame.visibility = View.INVISIBLE
            this.gameEnd = false
        }
    }

    private fun updateCurrentGameSharedPreference() {
        // Obtém uma instância do SharedPreferences
        val sharedPreferences = getSharedPreferences("GameScorerStatus", Context.MODE_PRIVATE)

        // Obtém um editor para modificar o SharedPreferences
        val editor = sharedPreferences.edit()

        // Salva o dado no SharedPreferences
        //Escrita em Bytes de Um objeto Serializável
        val dt = ByteArrayOutputStream()
        val oos = ObjectOutputStream(dt)

        oos.writeObject(this.score)
        editor.putString("score_status", dt.toString(StandardCharsets.ISO_8859_1.name()))
        editor.putString("darts_per_round", this.numPlaysByTurn.toString())
        editor.putString("playerA_plays", this.numPlaysAPlayer.toString())
        editor.putString("playerB_plays", this.numPlaysBPlayer.toString())

        editor.apply()
    }

    private fun clearCurrentGameSharedPreference() {
        // Obtém uma instância do SharedPreferences
        val sharedPreferences = getSharedPreferences("GameScorerStatus", Context.MODE_PRIVATE)

        // Obtém um editor para modificar o SharedPreferences
        val editor = sharedPreferences.edit()

        // Remove o dado associado à chave
        editor.remove("score_status")
        editor.remove("darts_per_round")
        editor.remove("playerA_plays")
        editor.remove("playerB_plays")

        // Confirma a remoção
        editor.apply()
    }

    private fun saveGame(dao: ScoreDAO, score: Score) {
//        launch {
        Thread(
            Runnable {
                Log.d("PDM23", "Indo adicionar dado ao banco de dados")
                dao.insert(score)
                Log.d("PDM23", "Dado adicionado ao banco de dados")
                val resp = dao.getAllScores()
                Log.d("PDM23", resp.toString())
            }
        ).start()


//        val sharedFilename = "PreviousGames"
//        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
//        var edShared = sp.edit()
//        //Salvar o número de jogos já armazenados
//        var numMatches= sp.getInt("numberMatch",0) + 1
//        edShared.putInt("numberMatch", numMatches)
//
//        //Escrita em Bytes de Um objeto Serializável
//        var dt= ByteArrayOutputStream()
//        var oos = ObjectOutputStream(dt);
//        oos.writeObject(this.score);
//
//        //Salvar como "match"
//        edShared.putString("match"+numMatches, dt.toString(StandardCharsets.ISO_8859_1.name()))
//        edShared.commit() //Não esqueçam de comitar!!!

    }
}