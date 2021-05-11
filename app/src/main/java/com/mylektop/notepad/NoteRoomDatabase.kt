package com.mylektop.notepad

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by iddangunawan on 08/05/21
 */
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
//                    .addCallback(NoteDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class NoteDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populatedDatabase(database.noteDao())
                    }
                }
            }
        }

        suspend fun populatedDatabase(noteDao: NoteDao) {
            noteDao.deleteAll()

            val currentTime: Date = Calendar.getInstance().time

            var note = Note(
                id = 1,
                title = "Note 1",
                content = "Note 1 content",
                updateAt = currentTime.toString()
            )
            noteDao.insert(note)

            note = Note(
                id = 2,
                title = "Note 2",
                content = "Note 2 content",
                updateAt = currentTime.toString()
            )
            noteDao.insert(note)

            note = Note(
                id = 3,
                title = "Note 3",
                content = "Note 3 content",
                updateAt = currentTime.toString()
            )
            noteDao.insert(note)
        }
    }
}