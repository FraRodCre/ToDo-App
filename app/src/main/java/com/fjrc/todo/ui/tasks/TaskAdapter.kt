package com.fjrc.todo.ui.tasks

import android.animation.ValueAnimator
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fjrc.todo.R
import com.fjrc.todo.data.model.Task
import com.fjrc.todo.util.DateHelper
import com.fjrc.todo.util.IconButton
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(val listener: Listener) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffUtil.getInstance()) {

    interface Listener {
        fun onTaskClicked(task: Task)
        fun onTaskMarked(task: Task, isDone: Boolean)
        fun onTaskLongClicked(task: Task)
        fun onTaskHighPriorityMarked(task: Task, isHighPriority: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(task: Task) {
            with(itemView) {
                if (task.isDone) {
                    applyStrikethrough(textContent, task.content)
                } else {
                    removeStrikethrough(textContent, task.content)
                }
                applyColorToHighPriority(itemView.findViewById(R.id.buttonHighPriority), task.isHighPriority)

                textDate.text = DateHelper.calculateTimeAgo(task.createdAt)

                checkHighPriorityDet.isChecked = task.isDone

                if (task.idParentTask == null) {
                    setOnClickListener {
                        listener.onTaskClicked(task)
                    }
                }

                setOnLongClickListener {
                    listener.onTaskLongClicked(task)
                    true
                }
                checkHighPriorityDet.setOnClickListener {
                    val isChecked = (it as CheckBox).isChecked

                    listener.onTaskMarked(task, isChecked)

                    it.animate()
                        .rotationBy(360f)
                        .setDuration(300)
                        .setInterpolator(FastOutSlowInInterpolator())
                        .start()

                    executeAnimation(itemView, isChecked)
                }
                buttonHighPriority.setOnClickListener {
                    val isHighPriority = !task.isHighPriority

                    listener.onTaskHighPriorityMarked(task, isHighPriority)
                }
            }
        }

        private fun applyColorToHighPriority(view: IconButton, isHighPriority: Boolean) {
            if (isHighPriority) {
                view.setColorDrawable(Color.RED)
            } else {
                view.setColorDrawable(Color.WHITE)
            }
        }

        private fun executeAnimation(view: View, isDone: Boolean) {
            val textContent = view.findViewById<TextView>(R.id.textContent)
            val content = textContent.text.toString()

            if (isDone) {
                applyStrikethrough(textContent, content, animate = true)
            } else {
                removeStrikethrough(textContent, content, animate = true)
            }
        }

        private fun applyStrikethrough(view: TextView, content: String, animate: Boolean = false) {
            val span = SpannableString(content)
            val spanStrike = StrikethroughSpan()

            if (animate) {
                ValueAnimator.ofInt(content.length).apply {
                    duration = 300
                    interpolator = FastOutSlowInInterpolator()
                    addUpdateListener { newValue ->
                        span.setSpan(spanStrike, 0, newValue.animatedValue as Int, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.text = span
                    }
                }.start()

            } else {
                span.setSpan(spanStrike, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = span
            }
        }

        private fun removeStrikethrough(view: TextView, content: String, animate: Boolean = false) {
            val span = SpannableString(content)
            val spanStrike = StrikethroughSpan()

            if (animate) {
                ValueAnimator.ofInt(content.length, 0).apply {
                    duration = 300
                    interpolator = FastOutSlowInInterpolator()
                    addUpdateListener { newValue ->
                        span.setSpan(spanStrike, 0, newValue.animatedValue as Int, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.text = span
                    }
                }.start()

            } else {
                view.text = content
            }
        }

    }
}