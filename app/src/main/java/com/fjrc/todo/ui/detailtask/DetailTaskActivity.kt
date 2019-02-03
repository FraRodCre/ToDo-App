package com.fjrc.todo.ui.detailtask

import android.app.Activity
import android.os.Bundle
import com.fjrc.todo.R
import com.fjrc.todo.data.model.Task
import com.fjrc.todo.ui.base.BaseActivity
import com.fjrc.todo.ui.edittask.EditTaskFragment
import com.fjrc.todo.ui.tasks.TaskViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.fjrc.todo.ui.tasks.TaskFragment
import kotlinx.android.synthetic.main.activiy_detail_task.*

class DetailTaskActivity: BaseActivity(), EditTaskFragment.updateTaskOnDismissOfEdit {

    private val taskViewModel: TaskViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_detail_task)

        setUpToolbar(true)
        setTitle(R.string.title_activity_detail_task)

        task = intent.getParcelableExtra("taskSelected")
        if (task == null) {
            setResult(Activity.RESULT_OK)
            finish()
        }

        setUp()

        val bundle = Bundle()
        bundle.putLong("idParentTask", task!!.id)

        val fragment = TaskFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

    }

    override fun updateTaskOnDismissOfEdit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setUp() {
        fillData()
        bindEvents()
        bindActions()
    }

    private fun bindEvents() {
        with(taskViewModel) {
            taskUpdatedEvent.observe(this@DetailTaskActivity, Observer {
                finish()
            })
        }
    }

    private fun fillData() {
        requireNotNull(task) {
            "Task is null dialog should be closed"
        }

        textContent.setText(task!!.content)
        checkHighPriorityDet.isChecked = task!!.isHighPriority
    }

    private fun bindActions() {
        buttonSaveTask.setOnClickListener {
            val newTask = task!!.copy(
                content = inputTaskContent.text.toString(),
                isHighPriority = checkHighPriorityDet.isChecked
            )

            taskViewModel.updateTask(newTask)
        }
    }

}