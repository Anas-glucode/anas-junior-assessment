package com.example.taskmaster.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaster.domain.repos.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    var taskTitle by mutableStateOf("")
        private set

    var taskDescription by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            taskTitle = task.title

            taskDescription = task.description
        }
    }

    fun onTitleChange(newTitle: String) {
        taskTitle = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        taskDescription = newDescription
    }

    fun updateTask(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val existingTask = repository.getTaskById(taskId)
            existingTask?.let {
                val updatedTask = it.copy(
                    title = taskTitle,
                    description = taskDescription
                )
                repository.insertTask(updatedTask)
                onSuccess()
            }
        }
    }
}