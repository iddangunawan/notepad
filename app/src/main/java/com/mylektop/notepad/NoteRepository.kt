package com.mylektop.notepad

import androidx.annotation.WorkerThread
import com.mylektop.notepad.base.BaseState
import kotlinx.coroutines.flow.Flow

/**
 * Created by iddangunawan on 09/05/21
 */
interface NoteUseCase {
    suspend fun getNoteById(id: Int): BaseState<Note>
}

class NoteRepository(private val noteDao: NoteDao) : NoteUseCase {

    val allNotes: Flow<List<Note>> = noteDao.getAllOrderByUpdateAt()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun getNoteById(id: Int): BaseState<Note> {
        return try {
            val response = noteDao.getById(id)
            BaseState.SuccessResponse(response)
        } catch (e: Exception) {
            BaseState.FailedResponse(e.message ?: "FAILED")
        }
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