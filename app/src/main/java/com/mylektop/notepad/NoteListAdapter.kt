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
class NoteListAdapter(private val listener: NoteItemListener) :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NoteViewHolder.NotesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class NoteViewHolder(itemView: View, private val listener: NoteItemListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val noteItemViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val noteItemViewLastUpdate: TextView = itemView.findViewById(R.id.textViewUpdateAt)

        private lateinit var note: Note

        init {
            itemView.rootView.setOnClickListener(this)
        }

        fun bind(note: Note?) {
            this.note = note!!
            noteItemViewTitle.text = note.title
            noteItemViewLastUpdate.text = note.updateAt
        }

        override fun onClick(v: View?) {
            listener.onClickNote(note.id)
        }

        companion object {
            fun create(parent: ViewGroup, listener: NoteItemListener): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)

                return NoteViewHolder(view, listener)
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

    interface NoteItemListener {
        fun onClickNote(noteId: Int)
    }
}