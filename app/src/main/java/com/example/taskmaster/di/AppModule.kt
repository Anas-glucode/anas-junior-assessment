package com.example.taskmaster.di

import android.app.Application
import androidx.room3.ProvidedAutoMigrationSpec
import androidx.room3.Room
import com.example.taskmaster.data.local.database.TaskDatabase
import com.example.taskmaster.domain.repos.TaskRepositoryImpl
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides

@Module
@InstallIn(Singleton::class)
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

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.dao)
    }

}
