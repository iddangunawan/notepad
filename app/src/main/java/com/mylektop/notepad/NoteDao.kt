package com.mylektop.notepad

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by iddangunawan on 08/05/21
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY update_at ASC")
    fun getAllOrderByUpdateAt(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getById(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()
}