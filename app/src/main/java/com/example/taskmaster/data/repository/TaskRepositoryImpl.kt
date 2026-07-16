package com.example.taskmaster.data.repository

import android.R.attr.id
import com.example.taskmaster.data.local.database.TaskDao
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.repos.TaskRepository
import kotlinx.coroutines.flow.Flow


class TaskRepositoryImpl(
    private var dao: TaskDao
): TaskRepository {

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun getTaskById(id: Int): Task {
        return dao.getTaskById(id)
    }

    override fun getTask(): Flow<List<Task>> {
        return dao.getTask()
    }
}