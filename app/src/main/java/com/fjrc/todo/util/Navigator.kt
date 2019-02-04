package com.fjrc.todo.util

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.fjrc.todo.data.model.Task
import com.fjrc.todo.ui.detailtask.DetailTaskActivity
import com.fjrc.todo.ui.edittask.EditTaskFragment
import com.fjrc.todo.ui.newtask.NewTaskActivity

object Navigator {

    fun navigateToNewTaskActivity(parentTaskId: Long,context: Context) {
        val intent = Intent(context, NewTaskActivity::class.java)
        intent.putExtra("idParentTask", parentTaskId)
        context.startActivity(intent)
    }

    fun navigateToEditTaskFragment(task: Task, fragmentManager: FragmentManager) : EditTaskFragment {
        val fragment = EditTaskFragment.newInstance(task)
        fragment.show(fragmentManager, null)
        return fragment
    }

    fun navigateToTaskDetailActivity(task: Task, context: Context) {
        val intent = Intent(context, DetailTaskActivity::class.java)
        intent.putExtra("task", task)
        context.startActivity(intent)
    }
}