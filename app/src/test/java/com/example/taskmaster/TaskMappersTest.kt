package com.example.taskmaster

import com.example.taskmaster.data.local.entities.TaskEntity
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.toDomain
import com.example.taskmaster.domain.models.toEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TaskMapperTest {

    @Test
    fun whenTaskHasNonNullId_toEntity_shouldMapAllFields() {
        val task =
            Task(id = 7, title = "Buy milk", description = "2% at the store", isCompleted = true)

        val entity = task.toEntity()

        assertEquals(7, entity.id)
        assertEquals("Buy milk", entity.title)
        assertEquals("2% at the store", entity.description)
        assertEquals(true, entity.isCompleted)
    }

    @Test
    fun whenTaskHasNullId_toEntity_shouldMapNullId() {
        val task = Task(id = null, title = "New task", description = "", isCompleted = false)

        val entity = task.toEntity()

        assertNull(entity.id)
    }

    @Test
    fun whenEntityHasNullDescription_toDomain_shouldReturnEmptyStringDescription() {
        val entity = TaskEntity(id = 1, title = "Call dentist", description = null, isCompleted = false)

        val task = entity.toDomain()

        assertEquals("", task.description)
    }

    @Test
    fun whenEntityHasNonNullDescription_toDomain_shouldPreserveDescription() {
        val entity = TaskEntity(id = 2, title = "Walk dog", description = "Twice today", isCompleted = true)

        val task = entity.toDomain()

        assertEquals("Twice today", task.description)
        assertEquals(true, task.isCompleted)
    }

    @Test
    fun whenRoundTrippedThroughToEntityAndToDomain_shouldPreserveOriginalTask() {
        val original =
            Task(id = 3, title = "Read book", description = "Chapter 5", isCompleted = false)

        val roundTripped = original.toEntity().toDomain()

        assertEquals(original, roundTripped)
    }
}