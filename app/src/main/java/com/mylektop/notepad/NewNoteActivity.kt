package com.mylektop.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import jp.wasabeef.richeditor.RichEditor
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var richEditorContentView: RichEditor

    private var contentValue: String? = null

    @SuppressLint("SimpleDateFormat")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        editTitleView = findViewById(R.id.edit_title)
        richEditorContentView = findViewById(R.id.editor)

        richEditorContentView.setEditorHeight(200)
        richEditorContentView.setEditorFontSize(18)
        richEditorContentView.setPadding(10, 10, 10, 10)
        richEditorContentView.setPlaceholder("Insert content here...")

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

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text) || TextUtils.isEmpty(contentValue)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = editTitleView.text.toString()
                val content = contentValue.toString()
                val calendar = Calendar.getInstance()
                val currentTime = calendar.time
                val df = SimpleDateFormat("EEE, dd MMM yyyy\nHH.mm")
                val formatDate = df.format(currentTime)

                replyIntent.putExtra(EXTRA_REPLY_TITLE, title)
                replyIntent.putExtra(EXTRA_REPLY_CONTENT, content)
                replyIntent.putExtra(EXTRA_REPLY_UPDATE_AT, formatDate)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.notelistsql.REPLY.TITLE"
        const val EXTRA_REPLY_CONTENT = "com.example.android.notelistsql.REPLY.CONTENT"
        const val EXTRA_REPLY_UPDATE_AT = "com.example.android.notelistsql.REPLY.UPDATE.AT"
    }
}