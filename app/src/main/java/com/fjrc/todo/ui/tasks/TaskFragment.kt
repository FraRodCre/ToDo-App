package com.fjrc.todo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjrc.todo.R
import com.fjrc.todo.data.model.Task
import com.fjrc.todo.util.Navigator
import com.fjrc.todo.util.bottomsheet.BottomMenuItem
import com.fjrc.todo.util.bottomsheet.BottomSheetMenu
import kotlinx.android.synthetic.main.fragment_tasks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskFragment : Fragment(), TaskAdapter.Listener {

    val taskViewModel: TaskViewModel by viewModel()

    val adapter: TaskAdapter by lazy {
        TaskAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUp()
    }

    private fun setUp() {
        setUpRecycler()

        arguments?.let {
            val idParentTask = it.getLong("idParentTask")
            if (idParentTask != 0L) {
                taskViewModel.loadChildTasks(idParentTask)
            } else {
                taskViewModel.loadTasks()
            }
        }

        with(taskViewModel) {
            tasksEvent.observe(this@TaskFragment, Observer { tasks ->
                adapter.submitList(tasks)
            })
        }
    }

    private fun setUpRecycler() {
        recyclerTasks.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerTasks.itemAnimator = DefaultItemAnimator()
        recyclerTasks.adapter = adapter
    }

    override fun onTaskClicked(task: Task) {
        Navigator.navigateToTaskDetailActivity(task, context!!)
    }

    override fun onTaskLongClicked(task: Task) {
        val items = arrayListOf(
            BottomMenuItem(R.drawable.ic_edit, getString(R.string.edit)) {
                Navigator.navigateToEditTaskFragment(task, childFragmentManager)
            },
            BottomMenuItem(R.drawable.ic_delete, getString(R.string.delete)) {
                showConfirmDeleteTaskDialog(task)
            }
        )

        if (task.idParentTask == null) {
            items.add(BottomMenuItem(R.drawable.ic_fab_add, getString(R.string.add_subtask)) {
                Navigator.navigateToNewTaskActivity(task.id, context!!)
            })
        }

        BottomSheetMenu(activity!!, items).show()
    }

    private fun showConfirmDeleteTaskDialog(task: Task) {
        AlertDialog.Builder(activity!!)
            .setTitle(R.string.delete_task_title)
            .setMessage(R.string.delete_task_message)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
            .show()
    }

    override fun onTaskMarked(task: Task, isDone: Boolean) {
        if (isDone) {
            taskViewModel.markAsDone(task)
        } else {
            taskViewModel.markAsNotDone(task)
        }
    }

    override fun onTaskHighPriorityMarked(task: Task, isHighPriority: Boolean) {
        taskViewModel.markHighPriority(task, isHighPriority)
    }

}
