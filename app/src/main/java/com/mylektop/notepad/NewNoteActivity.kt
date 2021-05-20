package com.mylektop.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import jp.wasabeef.richeditor.RichEditor
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var richEditorContentView: RichEditor
    private lateinit var buttonPrimary: Button
    private lateinit var buttonSecondary: Button

    private var note: Note? = null
    private var contentValue: String? = null

    @SuppressLint("SimpleDateFormat")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.title = getString(R.string.add_note)

        editTitleView = findViewById(R.id.edit_title)
        richEditorContentView = findViewById(R.id.editor)
        buttonPrimary = findViewById(R.id.button_primary)
        buttonSecondary = findViewById(R.id.button_secondary)

        richEditorContentView.setEditorHeight(200)
        richEditorContentView.setEditorFontSize(18)
        richEditorContentView.setPadding(10, 10, 10, 10)
        richEditorContentView.setPlaceholder(getString(R.string.hint_content))
        richEditorContentView.setOnTextChangeListener { text ->
            contentValue = text
        }

        findViewById<ImageButton>(R.id.action_bold).setOnClickListener {
            richEditorContentView.setBold()
        }

        findViewById<ImageButton>(R.id.action_italic).setOnClickListener {
            richEditorContentView.setItalic()
        }

        findViewById<ImageButton>(R.id.action_underline).setOnClickListener {
            richEditorContentView.setUnderline()
        }

        findViewById<ImageButton>(R.id.action_insert_numbers).setOnClickListener {
            richEditorContentView.setNumbers()
        }

        findViewById<ImageButton>(R.id.action_insert_bullets).setOnClickListener {
            richEditorContentView.setBullets()
        }

        buttonPrimary.setOnClickListener {
            when {
                TextUtils.isEmpty(editTitleView.text) -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.field_cannot_be_blank, "Title"),
                        Toast.LENGTH_LONG
                    ).show()
                    editTitleView.requestFocus()
                }
                TextUtils.isEmpty(contentValue) -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.field_cannot_be_blank, "Content"),
                        Toast.LENGTH_LONG
                    ).show()
                    richEditorContentView.requestFocus()
                }
                else -> {
                    val replyIntent = Intent()
                    val id = note?.id ?: 0
                    val title = editTitleView.text.toString()
                    val content = contentValue.toString()
                    val calendar = Calendar.getInstance()
                    val currentTime = calendar.time
                    val df = SimpleDateFormat("EEE, dd MMM yyyy\nHH.mm")
                    val formatDate = df.format(currentTime)

                    note = Note(id, title, content, formatDate)
                    replyIntent.putExtra(EXTRA_REPLY_NOTE, note)
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }

        buttonSecondary.visibility = View.GONE

        note = intent?.getSerializableExtra(EXTRA_REPLY_NOTE) as Note?
        if (note != null) {
            supportActionBar?.title = getString(R.string.edit_note)
            editTitleView.setText(note?.title)
            richEditorContentView.html = note?.content
            contentValue = note?.content
            buttonPrimary.setText(R.string.btn_update)
            buttonSecondary.visibility = View.VISIBLE
            buttonSecondary.setOnClickListener {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_REPLY_NOTE, note)
                replyIntent.putExtra(EXTRA_REPLY_ACTION, EXTRA_REPLY_ACTION_DELETE)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_REPLY_NOTE = "EXTRA_REPLY_NOTE"
        const val EXTRA_REPLY_ACTION = "EXTRA_REPLY_ACTION"
        const val EXTRA_REPLY_ACTION_DELETE = "EXTRA_REPLY_ACTION_DELETE"
    }
}