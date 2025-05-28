package hr.foi.rmai.memento.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.services.TaskTimerService
import java.text.SimpleDateFormat
import java.util.Date

class TasksAdapter(val tasksList: MutableList<Task>, private val onTaskCompleted: ((taskId: Int) -> Unit)? = null) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskName: TextView
        private val taskDueDate: TextView
        private val taskCourseColor: SurfaceView

        private val sdf = SimpleDateFormat("dd. MM. yyyy. HH:mm")
        private val taskTimer: ImageView = view.findViewById(R.id.iv_task_timer)
        private var isTimerActive = false

        init {
            taskName = view.findViewById(R.id.tv_task_name)
            taskDueDate = view.findViewById(R.id.tv_task_due_date)
            taskCourseColor = view.findViewById(R.id.sv_task_course_color)

            view.setOnClickListener {
                if (Date() < tasksList[adapterPosition].dueDate) {
                    val intent = Intent(view.context, TaskTimerService::class.java).apply {
                        putExtra("task_id", tasksList[adapterPosition].id.toLong())
                    }
                    isTimerActive = !isTimerActive
                    if (isTimerActive) {
                        taskTimer.visibility = View.VISIBLE
                    } else {
                        intent.putExtra("cancel", true)
                        taskTimer.visibility = View.GONE
                    }
                    view.context.startService(intent)
                } else if (taskTimer.visibility == View.VISIBLE) {
                    taskTimer.visibility = View.GONE
                }
            }

            view.setOnLongClickListener {
                val currentTask = tasksList[adapterPosition]
                val alertDialogBuilder = AlertDialog.Builder(view.context)
                    .setTitle(taskName.text)
                    .setNeutralButton("Delete task") { _, _ ->
                        TasksDatabase.getInstance().getTasksDao().removeTask(currentTask)
                        removeTaskFromList()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }

                if (onTaskCompleted != null) {
                    alertDialogBuilder.setPositiveButton("Mark as completed") { _, _ ->
                        var completedTask = tasksList[adapterPosition]
                        completedTask.completed = true
                        TasksDatabase.getInstance().getTasksDao().insertTask(completedTask)
                        removeTaskFromList()

                        onTaskCompleted.invoke(completedTask.id)
                    }
                }

                alertDialogBuilder.show()
                return@setOnLongClickListener true
            }
        }

        fun bind(task: Task) {
            taskName.text = task.name
            taskDueDate.text = sdf.format(task.dueDate)
            taskCourseColor.setBackgroundColor(task.course.color.toColorInt())
        }

        private fun removeTaskFromList() {
            tasksList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    fun addTask(newTask: Task) {
        var newIndexInList = tasksList.indexOfFirst { task ->
            task.dueDate > newTask.dueDate
        }

        if (newIndexInList == -1) {
            newIndexInList = tasksList.size
        }

        tasksList.add(newIndexInList, newTask)
        notifyItemInserted(newIndexInList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater.from(parent.context)
                            .inflate(R.layout.task_list_item, parent, false)

        return TaskViewHolder(taskView)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }
}