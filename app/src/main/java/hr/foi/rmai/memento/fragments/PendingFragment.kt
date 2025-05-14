package hr.foi.rmai.memento.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.helpers.MockDataLoader
import hr.foi.rmai.memento.helpers.NewTaskDialogHelper

class PendingFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var btnCreateTask: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mockTasks = MockDataLoader.getDemoData()
        mockTasks.forEach { task -> Log.i("MOCK_TASKS", task.name)}

        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_pending_tasks)
        recyclerView.adapter = TasksAdapter(MockDataLoader.getDemoData())
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
                val newTask = dialogHelper.buildTask()

                val tasksAdapter = recyclerView.adapter
                        as TasksAdapter
                tasksAdapter.addTask(newTask)
            }
            .show()

        dialogHelper.populateSpinner(MockDataLoader.getMockCourses())
        dialogHelper.activateDateTimeListeners()
    }
}