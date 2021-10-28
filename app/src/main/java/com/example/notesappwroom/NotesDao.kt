package com.example.notesappwroom

import androidx.room.*


@Dao
interface NotesDao {

    //On conflict deals with duplicate data so if we have duplicate data we chose to ignore it - we can replace it and many other thing
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note : Notes)

    // For retrieving data - it returns a fixed number of notes and we won't change it that's why we put it list not arraylist
    @Query("SELECT * FROM notes ORDER BY pk ASC")
    fun getNotes(): List<Notes>

    @Update
    suspend fun updateNote(note : Notes)

    @Delete
    suspend fun deleteNote(note : Notes)
}