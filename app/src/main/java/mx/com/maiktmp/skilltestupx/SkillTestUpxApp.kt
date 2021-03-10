package mx.com.maiktmp.skilltestupx

import android.app.Application
import mx.com.maiktmp.database.DBSkillTestUpx

class SkillTestUpxApp : Application() {


    override fun onCreate() {
        super.onCreate()
        createDatabase()
    }

    private fun createDatabase() {
        DBSkillTestUpx.createDatabase(this)
    }
}