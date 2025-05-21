package hr.foi.rmai.memento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.database.TasksDatabase

class CompletedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_completed_tasks)

        val completedTasks = TasksDatabase.getInstance().getTasksDao().getAllTasks(true)
        recyclerView.adapter = TasksAdapter(completedTasks.toMutableList())
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        parentFragmentManager.setFragmentResultListener("task_completed", viewLifecycleOwner) { _, bundle ->
            val addedTaskId = bundle.getInt("task_id")
            val tasksAdapter = recyclerView.adapter as TasksAdapter
            tasksAdapter.addTask(TasksDatabase.getInstance().getTasksDao().getTask(addedTaskId))
        }
    }
}
