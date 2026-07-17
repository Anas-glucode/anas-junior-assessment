package com.example.taskmaster.data.repository

import com.example.taskmaster.data.local.database.TaskDao
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.toEntity
import com.example.taskmaster.domain.models.toDomain
import com.example.taskmaster.domain.repos.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val dao: TaskDao // Best practice to use 'val' instead of 'var'
): TaskRepository {

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }

    override suspend fun getTaskById(id: Int): Task {
        // Handle null case gracefully if ID doesn't exist
        return dao.getTaskById(id)?.toDomain() ?: Task(title = "", description = "", isCompleted = false)
    }

    override fun getTask(): Flow<List<Task>> {
        return dao.getTask().map { entityList ->
            entityList.map { it.toDomain() }
        }
    }
}