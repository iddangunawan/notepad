package com.mylektop.notepad

import androidx.lifecycle.*
import com.mylektop.notepad.base.BaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by iddangunawan on 09/05/21
 */
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    val eventGlobalMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val getNoteByIdEvent: MutableLiveData<Note> by lazy {
        MutableLiveData<Note>()
    }

    val allNotes: LiveData<List<Note>> = repository.allNotes.asLiveData()

    fun getNoteById(id: Int) = viewModelScope.launch {
        val response = repository.getNoteById(id)
        withContext(Dispatchers.IO) {
            when (response) {
                is BaseState.SuccessResponse -> getNoteByIdEvent.postValue(response.data)
                is BaseState.FailedResponse -> eventGlobalMessage.postValue(response.message)
            }
        }
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}