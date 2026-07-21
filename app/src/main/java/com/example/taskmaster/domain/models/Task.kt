package com.example.taskmaster.domain.models

import com.example.taskmaster.data.local.entities.TaskEntity

data class Task(
    val id: Int? = null,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description ?: "",
        isCompleted = this.isCompleted
    )
}