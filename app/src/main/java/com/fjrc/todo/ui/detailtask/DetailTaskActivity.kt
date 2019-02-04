package com.fjrc.todo.ui.detailtask

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.fjrc.todo.R
import com.fjrc.todo.data.model.Task
import com.fjrc.todo.ui.base.BaseActivity
import com.fjrc.todo.ui.edittask.EditTaskFragment
import com.fjrc.todo.ui.tasks.TaskFragment
import com.fjrc.todo.ui.tasks.TaskViewModel
import com.fjrc.todo.util.DateHelper
import com.fjrc.todo.util.Navigator
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activiy_detail_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailTaskActivity : BaseActivity(), EditTaskFragment.updateTaskOnDismissOfEdit {

    private val taskViewModel: TaskViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_detail_task)

        setUpToolbar(true)
        setTitle(R.string.title_activity_detail_task)

        task = intent.getParcelableExtra("task")
        if (task == null) {
            setResult(Activity.RESULT_OK)
            finish()
        }

        setUp()

        val fragment = TaskFragment()
        val bundle = Bundle()
        bundle.putLong("idParentTask", task!!.id)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

    }

    override fun updateTaskOnDismissOfEdit() {
        taskViewModel.getTask(task!!.id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.action_edit_detail_task -> {
            val fragment = Navigator.navigateToEditTaskFragment(task!!, supportFragmentManager)
            fragment.setUpdateTaskOnDismissOfEdit(this)
            true
        }

        R.id.action_delete_detail_task -> {
            showConfirmDeleteTask(task!!)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun showConfirmDeleteTask(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_task_title)
            .setMessage(R.string.delete_task_message)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
            .show()
    }

    private fun setUp() {
        fillData()
        bindEvents()
        bindActions()
    }

    private fun bindEvents() {

        with(taskViewModel) {
            taskDeletedEvent.observe(this@DetailTaskActivity, Observer {
                setResult(Activity.RESULT_OK)
                finish()
            })

            taskEvent.observe(this@DetailTaskActivity, Observer {
                if (!it.hasBeenHandled) {
                    task = it.getContentIfNotHandled()
                    fillData()
                }
            })
        }
    }

    private fun fillData() {
        requireNotNull(task) {
            "Task is null dialog should be closed"
        }

        textContentDetail.setText(task!!.content)
        textDateDetail.text = DateHelper.calculateTimeAgo(task!!.createdAt)
        checkHighPriorityDet.isChecked = task!!.isHighPriority

        when (task!!.isHighPriority) {
            true -> {
                textPriorityDetail.text = "Muy importante"
            }
            false -> {
                textPriorityDetail.text = "Menos importante"

            }
        }
    }

    private fun bindActions() {
        fabAddSubtaskDetail
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                Navigator.navigateToNewTaskActivity(task!!.id, this)
            }
            .addTo(compositeDisposable)

        checkHighPriorityDet.setOnClickListener {
            if (checkHighPriorityDet.isChecked) {
                taskViewModel.markAsDone(task!!)
            } else {
                taskViewModel.markAsNotDone(task!!)
            }
        }
    }

}