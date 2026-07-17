package com.example.taskmaster.domain.models

import com.example.taskmaster.data.local.entities.TaskEntity

data class Task(
    val id: Int? = null,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

// Extension functions to convert between the two layers easily
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