package com.fjrc.todo.data.repository

import com.fjrc.todo.data.model.Task
import com.fjrc.todo.data.repository.datasource.local.LocalDataSource
import io.reactivex.Flowable
import io.reactivex.Single

class TaskRepositoryImpl(val localDataSource: LocalDataSource) : TaskRepository {

    override fun observeAll(): Flowable<List<Task>> = localDataSource.observeAll()

    override fun observeChildTask(idParentTask: Long): Flowable<List<Task>> =
        localDataSource.observeChildTask(idParentTask)

    override fun getAll(): Single<List<Task>> = localDataSource.getAll()

    override fun getTaskById(taskId: Long): Single<Task> = localDataSource.getTaskById(taskId)

    override fun insert(task: Task) {
        localDataSource.insert(task)
    }

    override fun delete(task: Task) {
        localDataSource.delete(task)
    }

    override fun update(task: Task) {
        localDataSource.update(task)
    }

}