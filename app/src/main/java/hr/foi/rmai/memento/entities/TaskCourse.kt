package hr.foi.rmai.memento.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_courses")
data class TaskCourse(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val color: String
) {
    override fun toString(): String {
        return name
    }
}