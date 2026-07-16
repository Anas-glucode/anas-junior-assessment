package com.example.taskmaster.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.taskmaster.domain.models.Task

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

}