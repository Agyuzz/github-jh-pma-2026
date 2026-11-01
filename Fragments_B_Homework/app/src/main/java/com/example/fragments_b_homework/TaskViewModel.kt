package com.example.fragments_b_homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

data class Task(val title: String, val date: String, val done: Boolean = false)

fun dateKey(year: Int, month: Int, day: Int): String =
    "%04d-%02d-%02d".format(year, month + 1, day)   // month v Androidu začíná od 0

fun today(): String {
    val c = Calendar.getInstance()
    return dateKey(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
}

fun keyToMillis(key: String): Long {
    val p = key.split("-")
    val c = Calendar.getInstance()
    c.set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt())
    return c.timeInMillis
}

class TaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>(
        listOf(
            Task("Udělat úkol do mobilních aplikací", today()),
            Task("Nákup", today()),
            Task("Zavolat rodičům", today())
        )
    )
    val tasks: LiveData<List<Task>> = _tasks

    // den vybraný v kalendáři (výchozí je dnešek)
    val selectedDate = MutableLiveData(today())

    fun addTask(title: String, date: String) {
        _tasks.value = _tasks.value.orEmpty() + Task(title, date)
    }

    fun toggle(task: Task, done: Boolean) {
        _tasks.value = _tasks.value.orEmpty().map { if (it === task) it.copy(done = done) else it }
    }
}