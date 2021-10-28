package com.example.notesappwroom

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class RVAdapter  (val activity: MainActivity, var itemList : List<Notes>) : RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {

    private val notesDao by lazy { NotesDB.getDatabase(activity).notesDao() }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = itemList[position]

        holder.itemView.apply {
            tvItems.text = items.note

            bEdit.setOnClickListener {
                activity.dialog(items.pk)
                update(itemList)
            }

            bDel.setOnClickListener {
                var selectedItems = items.pk
                activity.deleteData(selectedItems)
                Log.d("RVAdapter ", "id equal $selectedItems")
                update(itemList)
            }
        }
    }

    override fun getItemCount() = itemList.size

    fun update(itemList : List<Notes>){
        this.itemList = itemList
        notifyDataSetChanged()
    }

}