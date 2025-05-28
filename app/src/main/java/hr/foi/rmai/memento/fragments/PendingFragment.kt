package hr.foi.rmai.memento.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.MockDataLoader
import hr.foi.rmai.memento.helpers.NewTaskDialogHelper

class PendingFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var btnCreateTask: FloatingActionButton

    val tasksDao = TasksDatabase.getInstance().getTasksDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_pending_tasks)

        val tasks = tasksDao.getAllTasks(false)
        recyclerView.adapter = TasksAdapter(tasks.toMutableList()) { taskId ->
            parentFragmentManager.setFragmentResult("task_completed", bundleOf("task_id" to taskId))
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        btnCreateTask = view.findViewById(R.id.fab_pending_fragment_create_task)
        btnCreateTask.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val newTaskDialogView = LayoutInflater.from(context).inflate(R.layout.new_task_dialog, null)

        val dialogHelper = NewTaskDialogHelper(newTaskDialogView)

        AlertDialog.Builder(requireContext())
            .setView(newTaskDialogView)
            .setPositiveButton("Create a task") { _, _ ->
                var newTask = dialogHelper.buildTask()

                val newTaskId = tasksDao.insertTask(newTask)[0]
                newTask = tasksDao.getTask(newTaskId.toInt())

                val tasksAdapter = recyclerView.adapter
                        as TasksAdapter
                tasksAdapter.addTask(newTask)

                incrementTasksCreatedCounter()
            }
            .show()

        val courses = TasksDatabase.getInstance().getTaskCoursesDao().getAllCourses()
        dialogHelper.populateSpinner(courses)
        dialogHelper.activateDateTimeListeners()
    }

    private fun incrementTasksCreatedCounter() {
        context?.getSharedPreferences("tasks_preferences", Context.MODE_PRIVATE)?.apply {
            val currentCount = getInt("tasks_created_counter", 0)
            edit().putInt("tasks_created_counter", currentCount + 1).apply()
        }
    }
}