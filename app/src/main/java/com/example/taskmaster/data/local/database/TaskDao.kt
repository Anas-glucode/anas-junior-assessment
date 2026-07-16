package com.example.taskmaster.data.local.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.taskmaster.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE id = :id")
     fun getTaskById(id: Int): Task

    @Query("SELECT * FROM task")
    fun getTask(): Flow<List<Task>>
}