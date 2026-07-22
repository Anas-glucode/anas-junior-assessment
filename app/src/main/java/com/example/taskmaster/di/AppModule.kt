package com.example.taskmaster.di

import android.app.Application
import androidx.room.Room
import com.example.taskmaster.data.local.database.TaskDao
import com.example.taskmaster.data.local.database.TaskDatabase
import com.example.taskmaster.data.remote.api.WeatherService
import com.example.taskmaster.data.repository.TaskRepositoryImpl
import com.example.taskmaster.domain.repos.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    @Provides
    @Singleton
    fun provideWeatherRepository(client: HttpClient): WeatherService {
        return WeatherService(client)
    }

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
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(dao)
    }
}