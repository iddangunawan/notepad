package com.mylektop.notepad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
            intent.putExtra("NOTE", note)
            startActivityForResult(intent, editNoteActivityRequestCode)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val id = data?.getIntExtra(NewNoteActivity.EXTRA_REPLY_ID, 0) ?: 0
        val title = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_TITLE) ?: ""
        val content = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_CONTENT) ?: ""
        val updateAt = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_UPDATE_AT) ?: ""

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val note = Note(id, title, content, updateAt)
            noteViewModel.insert(note)
        } else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val note = Note(id, title, content, updateAt)
            noteViewModel.update(note)
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickNote(noteId: Int) {
        noteViewModel.getNoteById(noteId)
    }
}