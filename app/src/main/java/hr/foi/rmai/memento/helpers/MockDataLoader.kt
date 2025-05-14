package hr.foi.rmai.memento.helpers

import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse
import java.util.Date

object MockDataLoader {
    fun getMockCourses(): List<TaskCourse> = listOf(
        TaskCourse("RMAI", "#3449eb"),
        TaskCourse("RWA", "#d0eb34"),
        TaskCourse("EP", "#eb7134")
    )

    fun getDemoData(): MutableList<Task> {
        val courses = getMockCourses()

        return mutableListOf(
            Task("Submit project proposal", Date(), courses[0], false),
            Task("Prepare for exercises", Date(), courses[0], false),
            Task("Work on 1st homework", Date(), courses[1], false),
        )
    }
}