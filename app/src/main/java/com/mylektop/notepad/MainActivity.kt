package com.mylektop.notepad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteListAdapter.NoteItemListener {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    private val newNoteActivityRequestCode = 1
    private val editNoteActivityRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val linearLayoutEmptySection = findViewById<LinearLayout>(R.id.linearLayoutEmptySection)

        val adapter = NoteListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        noteViewModel.eventGlobalMessage.observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
        noteViewModel.allNotes.observe(this) { notes ->
            notes.let {
                adapter.submitList(it)
            }

            if (notes.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                linearLayoutEmptySection.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                linearLayoutEmptySection.visibility = View.VISIBLE
            }
        }
        noteViewModel.getNoteByIdEvent.observe(this) { note ->
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            intent.putExtra(NewNoteActivity.EXTRA_REPLY_NOTE, note)
            startActivityForResult(intent, editNoteActivityRequestCode)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, R.string.note_deleted, Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val note = data?.getSerializableExtra(NewNoteActivity.EXTRA_REPLY_NOTE) as Note?
        if (note != null) {
            if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
                noteViewModel.insert(note)
            } else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
                noteViewModel.update(note)
                if (data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_ACTION) == NewNoteActivity.EXTRA_REPLY_ACTION_DELETE) {
                    noteViewModel.delete(note)
                    Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_LONG).show()
                } else {
                    println("Info: else action delete")
                }
            } else {
                println("Info: else action")
            }
        } else {
            println("Info: else note")
        }
    }

    override fun onClickNote(noteId: Int) {
        noteViewModel.getNoteById(noteId)
    }
}