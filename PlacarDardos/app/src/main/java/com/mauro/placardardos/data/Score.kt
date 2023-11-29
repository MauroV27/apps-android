package com.mauro.placardardos.data
import java.io.Serializable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity( tableName = "score_table" )
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var playerA:String,
    var playerB:String,
    var initScores:Int,
    var winner:String="None",
    var finalScorePlayerA:Int,
    var finalScorePlayerB:Int,
    var finalDistanceBtwPlayers : Int,
    var dayTimeGameFinished : String
) : Serializable
