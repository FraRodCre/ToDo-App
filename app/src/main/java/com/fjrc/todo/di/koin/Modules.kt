package com.fjrc.todo.di.koin


import androidx.room.Room
import com.fjrc.todo.data.model.mapper.TaskEntityMapper
import com.fjrc.todo.data.model.mapper.TaskMapper
import com.fjrc.todo.data.repository.TaskRepository
import com.fjrc.todo.data.repository.TaskRepositoryImpl
import com.fjrc.todo.data.repository.datasource.local.LocalDataSource
import com.fjrc.todo.data.repository.datasource.local.TodoDatabase
import com.fjrc.todo.ui.tasks.TaskViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single { TaskMapper() }

    single { TaskEntityMapper() }

    single<TodoDatabase> {
        Room.databaseBuilder(androidContext(), TodoDatabase::class.java, "todo.db")
            .build()
    }

    single { LocalDataSource(get(), get(), get()) }

    single<TaskRepository> { TaskRepositoryImpl(get()) }

    viewModel { TaskViewModel(get()) }

}