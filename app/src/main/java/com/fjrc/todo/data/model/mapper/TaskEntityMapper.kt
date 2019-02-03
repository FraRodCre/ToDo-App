package com.fjrc.todo.data.model.mapper

import com.fjrc.todo.data.model.Task
import com.fjrc.todo.data.repository.datasource.local.TaskEntity

class TaskEntityMapper : Mapper<Task, TaskEntity> {

    override fun transform(input: Task): TaskEntity =
        TaskEntity(
            input.id,
            input.content,
            input.createdAt,
            input.isDone,
            input.isHighPriority,
            input.idParentTask
        )

    override fun transformList(input: List<Task>): List<TaskEntity> =
        input.map { transform(it) }

}