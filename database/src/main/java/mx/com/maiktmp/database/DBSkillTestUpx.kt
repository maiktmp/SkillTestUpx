package mx.com.maiktmp.database

import android.content.Context
import androidx.room.*
import mx.com.maiktmp.database.dao.MovieDao
import mx.com.maiktmp.database.dao.MoviePagerDao
import mx.com.maiktmp.database.entities.MovieDB
import mx.com.maiktmp.database.entities.MoviePagerDB

@Database(
    entities = [
        MoviePagerDB::class,
        MovieDB::class,
    ],
    version = 3,
    exportSchema = false
)
abstract class DBSkillTestUpx : RoomDatabase() {
    abstract fun moviePagerDao(): MoviePagerDao

    abstract fun movieDao(): MovieDao


    companion object {
        @Volatile
        lateinit var db: DBSkillTestUpx

        public fun createDatabase(context: Context) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBSkillTestUpx::class.java,
                    "DBSkillTestUpx.db"
                ).fallbackToDestructiveMigration()
                    .build()
                db = instance
                instance
            }
        }
    }
}