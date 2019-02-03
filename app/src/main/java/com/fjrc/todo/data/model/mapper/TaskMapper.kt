package com.fjrc.todo.data.model.mapper

import com.fjrc.todo.data.model.Task
import com.fjrc.todo.data.repository.datasource.local.TaskEntity

class TaskMapper : Mapper<TaskEntity, Task> {

    override fun transform(input: TaskEntity): Task =
        Task(
            input.id,
            input.content,
            input.createdAt,
            input.isDone,
            input.isHighPriority,
            input.idParentTask
        )

    override fun transformList(input: List<TaskEntity>): List<Task> =
        input.map { transform(it) }

}