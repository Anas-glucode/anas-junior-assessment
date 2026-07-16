package com.example.taskmaster.domain.models

import com.example.taskmaster.data.local.entities.TaskEntity

class Task(
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val id: Int? = null) {

    fun Task.toEntity(): TaskEntity{
        return TaskEntity(
            title = this.title,
            description = this.description,
            isCompleted = this.isCompleted,
            id = this.id
        )
    }
}