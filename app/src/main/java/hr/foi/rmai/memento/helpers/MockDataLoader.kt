package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getMockCourses(): List<TaskCourse> = listOf(
        TaskCourse(0, "RMAI", "#3449eb"),
        TaskCourse(1, "RWA", "#d0eb34"),
        TaskCourse(2, "EP", "#eb7134")
    )

    fun getDemoData(): MutableList<Task> {
        val courses = getMockCourses()

        return mutableListOf(
            Task(0, "Submit project proposal", Date(), 0, false),
            Task(1, "Prepare for exercises", Date(), 0, false),
            Task(2, "Work on 1st homework", Date(), 1, false),
        )
    }
}