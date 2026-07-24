package com.example.taskmaster.domain.repos

import kotlinx.coroutines.flow.Flow
import com.example.taskmaster.domain.models.Task

interface TaskRepository {

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun getTaskById(id: Int): Task?

    fun getTask(): Flow<List<Task>>
}