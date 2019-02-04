package com.fjrc.todo.data.repository.datasource.local


import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id_parent_task"),
        onDelete = CASCADE
    )
    ],
    indices = [Index(value = ["id_parent_task"])]
)

data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean,
    @ColumnInfo(name = "is_high_priority")
    val isHighPriority: Boolean,
    @ColumnInfo(name = "id_parent_task")
    val idParentTask: Long?
)