package com.fjrc.todo.ui

import android.os.Bundle
import androidx.appcompat.widget.ButtonBarLayout
import com.jakewharton.rxbinding3.view.clicks
import com.fjrc.todo.R
import com.fjrc.todo.ui.base.BaseActivity
import com.fjrc.todo.ui.tasks.TaskFragment
import com.fjrc.todo.util.Navigator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolbar(false)
        setTitle(R.string.app_name)
        setUp()
    }

    private fun setUp() {
        bindActions()

        val fragmentTask = TaskFragment()
        val bundleWithParent = Bundle()
        bundleWithParent.putLong("idParentTask",0)
        fragmentTask.arguments = bundleWithParent

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragmentTask)
            .commit()
    }

    private fun bindActions() {
        fab
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                Navigator.navigateToNewTaskActivity(0,this)
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
