package com.mylektop.notepad

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Created by iddangunawan on 09/05/21
 */
class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAllOrderByUpdateAt()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getNoteById(id: Int) {
        noteDao.getById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        noteDao.deleteAll()
    }
}