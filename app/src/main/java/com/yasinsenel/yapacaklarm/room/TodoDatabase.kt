package com.yasinsenel.yapacaklarm.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yasinsenel.yapacaklarm.model.TodoData

@Database(entities = [TodoData::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDAO

    /*companion object{
        @Volatile private var instance : TodoDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance?: synchronized(lock){
            instance?: makeDatabase(context).also {
                instance = it
            }
        }
        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,TodoDatabase::class.java,"tododatabase"
        ).build()
    }*/
}