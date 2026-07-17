package com.example.taskmaster.data.local.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
)