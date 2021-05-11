package com.mylektop.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by iddangunawan on 10/05/21
 */
class NoteListAdapter :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NoteViewHolder.NotesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteItemViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val noteItemViewLastUpdate: TextView = itemView.findViewById(R.id.textViewUpdateAt)

        fun bind(note: Note?) {
            noteItemViewTitle.text = note?.title
            noteItemViewLastUpdate.text = note?.updateAt
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)

                return NoteViewHolder(view)
            }
        }

        class NotesComparator : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}