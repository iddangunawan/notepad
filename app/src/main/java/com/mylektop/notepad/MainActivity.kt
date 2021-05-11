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

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val linearLayoutEmptySection = findViewById<LinearLayout>(R.id.linearLayoutEmptySection)

        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_TITLE) ?: ""
            val content = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_CONTENT) ?: ""
            val updateAt = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_UPDATE_AT) ?: ""

            val note = Note(0, title, content, updateAt)
            noteViewModel.insert(note)
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}