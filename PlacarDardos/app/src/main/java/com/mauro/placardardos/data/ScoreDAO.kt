package com.mauro.placardardos.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDAO {

    @Insert
    fun insert(score: Score)

    @Query("DELETE FROM 'score_table'")
    fun deleteAll()

    @Query(value = "SELECT * FROM score_table")
    fun getAllScores(): List<Score>
}