package com.example.notesappwroom

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var rvItems : RecyclerView
    lateinit var eInput : EditText
    lateinit var bAdd : Button
    lateinit var rvAdapter: RVAdapter
    lateinit var notesArray : List<Notes>

    private val notesDao by lazy { NotesDB.getDatabase(this).notesDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvItems = findViewById(R.id.rvItems)
        eInput = findViewById(R.id.eInput)
        bAdd = findViewById(R.id.bAdd)

        notesArray = ArrayList()

        bAdd.setOnClickListener {
            val msg = eInput.text.toString()
            CoroutineScope(IO).launch {
                notesDao.addNote(Notes(0,msg))
            }
            CoroutineScope(IO).launch {
                val data = async {
                    notesDao.getNotes()
                }.await()
                if(data.isNotEmpty()){
                    notesArray = data
                    withContext(Main){
                        rvAdapter.update(notesArray)
                    }
                }else{
                    Log.e("MainActivity", "Unable to get data", )
                }
            }
            rvAdapter.update(notesArray)
        }



        rvAdapter = RVAdapter(this,notesArray)
        rvItems.adapter = rvAdapter
        rvItems.layoutManager = LinearLayoutManager(this)

    }

    fun updateNote(pk : Int, note : String){
        if (pk != null) {
            CoroutineScope(IO).launch {
                notesDao.updateNote(Notes(pk, note))
            }
        } else {
            Log.d("Main ", "pk is null")
        }

    }

    fun deleteData(pk : Int){
        if (pk != null) {
            CoroutineScope(IO).launch {
                notesDao.deleteNote(Notes(pk,""))
            }
        } else {
            Log.d("Main ", "pk is null")
        }
    }

    fun dialog(pk : Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new note"

        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> updateNote(pk, updatedNote.text.toString())
                rvAdapter.update(notesArray)

            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }



}