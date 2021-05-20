package com.mylektop.notepad

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by iddangunawan on 20/05/21
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, NoteRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNote() = runBlocking {
        val note = Note(0, "Title", "Content", "Time")
        noteDao.insert(note)
        val allNotes = noteDao.getAllOrderByUpdateAt().first()
        assertEquals(allNotes[0].title, note.title)
    }

    @Test
    @Throws(Exception::class)
    fun getAllNotes() = runBlocking {
        val note = Note(0, "Title", "Content", "Time")
        noteDao.insert(note)
        val note2 = Note(0, "Title 2", "Content 2", "Time 2")
        noteDao.insert(note2)
        val allNotes = noteDao.getAllOrderByUpdateAt().first()
        assertEquals(allNotes[0].title, note2.title)
        assertEquals(allNotes[1].title, note.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val note = Note(0, "Title", "Content", "Time")
        noteDao.insert(note)
        val note2 = Note(1, "Title 2", "Content 2", "Time 2")
        noteDao.insert(note2)
        noteDao.deleteAll()
        val allNotes = noteDao.getAllOrderByUpdateAt().first()
        assertTrue(allNotes.isEmpty())
    }

}