package com.fjrc.todo.data.repository

import com.fjrc.todo.data.model.Task
import io.reactivex.Flowable
import io.reactivex.Single

interface TaskRepository {

    fun observeAll(): Flowable<List<Task>>

    fun observeChildTask(idParentTask: Long): Flowable<List<Task>>

    fun getAll(): Single<List<Task>>

    fun getTaskById(taskId: Long): Single<Task>

    fun insert(task: Task)

    fun delete(task: Task)

    fun update(task: Task)

}