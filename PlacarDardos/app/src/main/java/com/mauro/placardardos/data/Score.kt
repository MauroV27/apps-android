package com.mauro.placardardos.data
import java.io.Serializable

data class Score(
    var playerA:String,
    var playerB:String,
    var initScores:Int,
    var winner:String="None",
    var finalScorePlayerA:Int,
    var finalScorePlayerB:Int,
    var finalDistanceBtwPlayers : Int,
    var dayTimeGameFinished : String
):Serializable
