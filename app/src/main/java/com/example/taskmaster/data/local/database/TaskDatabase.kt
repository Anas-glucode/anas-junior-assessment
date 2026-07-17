package com.example.taskmaster.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.taskmaster.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class], // Fixed to point to your Entity
    version = 1
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}