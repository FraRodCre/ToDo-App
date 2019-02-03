package com.fjrc.todo.ui.newtask

import android.os.Bundle
import androidx.lifecycle.Observer
import com.fjrc.todo.R
import com.fjrc.todo.ui.base.BaseActivity
import com.fjrc.todo.ui.tasks.TaskViewModel
import kotlinx.android.synthetic.main.activity_new_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewTaskActivity : BaseActivity() {

    val taskViewModel: TaskViewModel by viewModel()

    private var idParentTask: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        setUpToolbar(true)
        setTitle(R.string.new_task_title)
        idParentTask = intent.getLongExtra("idParentTask",0)

        bindObserver()
        bindActions()
    }

    private fun bindObserver() {
        with(taskViewModel) {
            newTaskAddedEvent.observe(this@NewTaskActivity, Observer {
                if (!it.hasBeenHandled) {
                    it.getContentIfNotHandled()

                    setResult(android.app.Activity.RESULT_OK)
                    finish()
                }
            })
        }
    }

    private fun bindActions() {
        buttonSaveTask.setOnClickListener {
            taskViewModel.addNewTask(inputTaskContent.text.toString(), checkHighPriorityDet.isChecked, idParentTask)
        }
    }

}