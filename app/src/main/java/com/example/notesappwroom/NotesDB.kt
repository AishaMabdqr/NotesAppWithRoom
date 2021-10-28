package com.example.notesappwroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDB : RoomDatabase() {

    //To have access for the methods
    abstract fun notesDao() : NotesDao

    companion object{

        @Volatile //It works with threading so we only have  one thread and one change at a time
        private var INSTANCE: NotesDB? = null

        fun getDatabase(context : Context): NotesDB{
            // We return an instance if it's exist
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            //Or we create a new one
            synchronized(this) { // has to work with threading to make sure we don't create duplicates
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDB::class.java,
                    "notes"
                ).fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance

            }

        }

    }
}