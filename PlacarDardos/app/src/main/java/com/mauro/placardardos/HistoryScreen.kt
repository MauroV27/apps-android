package com.mauro.placardardos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mauro.placardardos.adapters.CardHistoryAdapter
import com.mauro.placardardos.data.Score
import com.mauro.placardardos.data.ScoreDAO
import com.mauro.placardardos.data.ScoreRoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HistoryScreen : AppCompatActivity() {

//    private lateinit var dao: ScoreDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_screen)

        // Trazendo o Recycler View
        val recyclerview = findViewById<RecyclerView>(R.id.rcPreviousGames)
        Log.d("PDM23", "Leu 1")

        // Tipo de Layout Manager será Linear
        recyclerview.layoutManager = LinearLayoutManager(this)
        Log.d("PDM23", "Leu 2")

        // Conecta com o banco de dados :
//        this.dao = ScoreRoomDB.getInstance(this).scoreDAO()

        Log.d("PDM23", "Leu 4")
//        readDataFromDB( ScoreRoomDB().scoreDAO().getAllScores() )

        val db = Room.databaseBuilder(
            this,
            ScoreRoomDB::class.java, ScoreRoomDB.DB_NAME
        )

        Log.d("PDM23", "DB value : ${db.toString()}")

        val dbBuild = db.build()

        Log.d("PDM23", "DB Build value : ${dbBuild.toString()}")

        val dao = dbBuild.scoreDAO()
        Log.d("PDM23", "DAO value : ${dao.toString()}")

        Thread(
            Runnable {

                val data : List<Score>? = dao?.getAllScores()
                Log.d("PDM23", "Valor data : ${data.toString()}")

                if (data != null){
                    Log.d("PDM23", "Leu 5")
                    // ArrayList enviado ao Adapter
                    val adapter = CardHistoryAdapter(data, this)
                    Log.d("PDM23", "Leu 6")
                    // Setando o Adapter no Recyclerview
                    recyclerview.adapter = adapter
                    Log.d("PDM23", "Leu 7")
                }

            }
        ).start()

    }

//    fun readPLacarDataSharedPreferences(): ArrayList<Score> {
//        Log.v("PDM", "Lendo o Shared Preferences")
//        val data = ArrayList<Score>()
//        val sharedFileName = "PreviousGames"
//        var aux: String
//        var sp: SharedPreferences = getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
//        if (sp != null) {
//            var numMatches = sp.getInt("numberMatch", 0)
//            Log.v("PDM", "numMatchs:" + numMatches)
//            for (i in 1..numMatches) {
//                aux = sp.getString("match" + i, "vazio")!!
//                if (!aux.equals("vazio")) {
//
//                    var bis: ByteArrayInputStream
//                    bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
//                    var obi: ObjectInputStream
//                    obi = ObjectInputStream(bis)
//
//                    var placar: Score = obi.readObject() as Score
//                    data.add(placar)
//                    //Log.v("PDM", "match"+i+" :"+aux)
//                    Log.v("PDM", placar.toString())
//                }
//            }
//        }
//        return data
//    }

//    private fun readDataFromDB(dao: ScoreDAO) = runBlocking{
//        // Inicia a corrotina dentro do contexto da atividade
//        Log.d("PDM23", "Indo ler os dados do banco de dados")
//        val job = fetchScores(dao)
//        Log.d("PDM23", "AAAA :")
//        Log.d("PDM23", job.toString())
////        return@runBlocking
//    }

//    suspend fun fetchScores(dao : ScoreDAO) : Job = coroutineScope {
//
//        return@coroutineScope launch {
//            dao.getAllScores()
//        }
//    }

    suspend fun fetchScores(dao : ScoreDAO) : List<Score> {
        val resp = withContext(Dispatchers.IO) {
            return@withContext dao.getAllScores()
        }

        Log.d("PDM23", "resposta de fetchScore: ")
        Log.d("PDM23", resp.toString())

        return resp
    }

//        return@withContext suspendCoroutine { continuation ->
//            dao.getAllScores { scores, error ->
//                if (error != null) {
//                    // Em caso de erro, retorna a exceção
//                    continuation.resumeWithException(error as Throwable)
//                } else {
//                    // Retorna os dados obtidos do DAO
//                    continuation.resume(scores as ArrayList<Score>)
//                }
//            }
//        }
//    }
}