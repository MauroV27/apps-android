package com.mauro.placardardos.adapters

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mauro.placardardos.R
import com.mauro.placardardos.data.Score

class CardHistoryAdapter(private val mList: List<Score>) : RecyclerView.Adapter<CardHistoryAdapter.CardViewHolder>(){

    // Criação de Novos ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        // infla o card_previous_games
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_history_element, parent, false)
        Log.d("PDM23", "Chamou o ViewHolder")

        return CardViewHolder(view)
    }

    // Ligando o Recycler view a um View Holder
    inner class CardViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val imageView: ImageView = ItemView.findViewById(R.id.imageview)
        val tvPLayerName: TextView = ItemView.findViewById(R.id.tvPlayerNames)
        val tvInitScore : TextView = itemView.findViewById(R.id.tvCardInitScore)

        val tvWinTextResult: TextView = ItemView.findViewById(R.id.tvWinTextDescription)
        val tvDateGame : TextView = itemView.findViewById(R.id.tvDateGame)
        val inCell : LinearLayout = itemView.findViewById(R.id.inCell)
    }

    // faz o bind de uma ViewHolder a um Objeto da Lista
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

//        if ( position < 0 || position > this.itemCount){
//            return
//        }

        val lastScore = mList[position]

        Log.d("PDM23", lastScore.toString())

//        val playersStringVs : String = Resources.getSystem().getString(R.string.txt_format_vs_players)//, lastScore.playerA, lastScore.playerB)
//        val playersStringVs : String = getResourceString(R.string.txt_format_vs_players)

//        Log.d("PDM23", "String resp : $playersStringVs")
//
//        //alimentando os elementos a partir do objeto placar
        holder.tvPLayerName.text = getResourceString(R.string.txt_format_vs_players).format(lastScore.playerA, lastScore.playerB)
        holder.tvWinTextResult.text = getResourceString(R.string.txt_winner_message).format(lastScore.winner, lastScore.finalDistanceBtwPlayers.toString())
//
        holder.tvInitScore.text = lastScore.initScores.toString()
        holder.tvInitScore.contentDescription = getResourceString(R.string.txt_scores_description_value).format(lastScore.initScores.toString())

//        holder.tvDateGame.text = lastScore.dayTimeGameFinished.toString()

//        holder.inCell.setOnClickListener{
//            val duration= Snackbar.LENGTH_LONG
//            val text= lastScore.dayTimeGameFinished
//
//            val snack= Snackbar.make(holder.inCell,text,duration)
//            snack.show()
//
//        }
    }

    private fun getResourceString(id: Int) : String {
        return Resources.getSystem().getString(id)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

}