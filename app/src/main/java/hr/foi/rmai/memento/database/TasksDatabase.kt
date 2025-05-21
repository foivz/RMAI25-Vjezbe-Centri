package hr.foi.rmai.memento.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.rmai.memento.entities.Task
import hr.foi.rmai.memento.entities.TaskCourse

@Database(version = 1, entities = [Task::class, TaskCourse::class], exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun getTasksDao(): TasksDAO
    abstract fun getTaskCoursesDao(): TaskCoursesDAO
}