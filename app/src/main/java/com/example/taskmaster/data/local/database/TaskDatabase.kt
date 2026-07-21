package com.example.taskmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmaster.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}