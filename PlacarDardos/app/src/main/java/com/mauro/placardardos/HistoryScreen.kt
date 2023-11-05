package com.mauro.placardardos

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mauro.placardardos.adapters.CardHistoryAdapter
import com.mauro.placardardos.data.Score
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class HistoryScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_screen)

        // Trazendo o Recycler View
        val recyclerview = findViewById<RecyclerView>(R.id.rcPreviousGames)

        // Tipo de Layout Manager será Linear
        recyclerview.layoutManager = LinearLayoutManager(this)

        Log.d("PDM23", "Leu 1")

        // O ArrayList de Placares
        val data = readPLacarDataSharedPreferences()
//        val data = simulateReadData()
        Log.d("PDM23", "Leu 2")

        // ArrayList enviado ao Adapter
        val adapter = CardHistoryAdapter(data, this)

        Log.d("PDM23", "Leu 3")

        // Setando o Adapter no Recyclerview
        recyclerview.adapter = adapter

        Log.d("PDM23", "Leu 4")
    }

    fun readPLacarDataSharedPreferences(): ArrayList<Score> {
        Log.v("PDM", "Lendo o Shared Preferences")
        val data = ArrayList<Score>()
        val sharedFileName = "PreviousGames"
        var aux: String
        var sp: SharedPreferences = getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        if (sp != null) {
            var numMatches = sp.getInt("numberMatch", 0)
            Log.v("PDM", "numMatchs:" + numMatches)
            for (i in 1..numMatches) {
                aux = sp.getString("match" + i, "vazio")!!
                if (!aux.equals("vazio")) {

                    var bis: ByteArrayInputStream
                    bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
                    var obi: ObjectInputStream
                    obi = ObjectInputStream(bis)

                    var placar: Score = obi.readObject() as Score
                    data.add(placar)
                    //Log.v("PDM", "match"+i+" :"+aux)
                    Log.v("PDM", placar.toString())
                }
            }
        }
        return data
    }

    fun simulateReadData() : ArrayList<Score> {
        Log.v("PDM", "Lendo o Shared Preferences")
        val data = ArrayList<Score>()

        for ( i in 1..5) {
            data.add(Score("Alberto", "Bruno", 501, "Alberto", 0, 22, 22, "AAAAA"))
            data.add(Score("João", "Maria", 301, "Maria", 32, 0, 32, "BB"))
        }

        return data
    }
}