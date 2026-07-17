package com.example.taskmaster.di

import android.app.Application
import androidx.room3.Room
import com.example.taskmaster.data.local.database.TaskDao
import com.example.taskmaster.data.local.database.TaskDatabase
import com.example.taskmaster.data.repository.TaskRepositoryImpl
import com.example.taskmaster.domain.repos.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }

    // 1. Provide the DAO explicitly by invoking the abstract function taskDao()
    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }

    // 2. Inject the TaskDao directly into the Repository implementation
    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(dao)
    }
}