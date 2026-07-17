package com.example.taskmaster.data.local.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.taskmaster.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity) // Uses TaskEntity

    @Delete
    suspend fun deleteTask(task: TaskEntity) // Uses TaskEntity

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity? // Added suspend and changed return type

    @Query("SELECT * FROM TaskEntity")
    fun getTask(): Flow<List<TaskEntity>> // Returns Entity flow
}