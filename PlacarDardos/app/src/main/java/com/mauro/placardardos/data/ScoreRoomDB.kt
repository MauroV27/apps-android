package com.mauro.placardardos.data
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Score::class], version = 1, exportSchema = false)
abstract class ScoreRoomDB : RoomDatabase() {

    companion object {
        const val DB_NAME = "score_database"
    }

    abstract fun scoreDAO(): ScoreDAO

    // Pegeui o código abaixo que transforma essa classe num singleton de :
    // https://github.com/tutorialseu/RoomDatabaseDemo/blob/169_completing_the_demo/app/src/main/java/eu/tutorials/roomdemo/EmployeeDatabase.kt
//    companion object {
//
//        @Volatile
//        private var INSTANCE: ScoreRoomDB? = null
//
//        fun getInstance(context: Context): ScoreRoomDB {
//
//            synchronized(this) {
//
//                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
//                // Smart cast is only available to local variables.
//                var instance = INSTANCE
//
//                // If instance is `null` make a new database instance.
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        ScoreRoomDB::class.java,
//                        "score_database"
//                    )
//                        // Wipes and rebuilds instead of migrating if no Migration object.
//                        // Migration is not part of this lesson. You can learn more about
//                        // migration with Room in this blog post:
//                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    // Assign INSTANCE to the newly created database.
//                    INSTANCE = instance
//                }
//
//                // Return instance; smart cast to be non-null.
//                return instance
//            }
//        }
//    }

}