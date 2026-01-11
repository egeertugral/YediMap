package com.example.yedimap.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yedimap.data.repository.ScheduleRepository
import com.example.yedimap.data.room.entity.ScheduleCell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ScheduleRepository(app.applicationContext)

    private val _cells = MutableStateFlow<List<ScheduleCell>>(emptyList())
    val cells: StateFlow<List<ScheduleCell>> = _cells

    fun load(studentNo: String) {
        viewModelScope.launch {
            repo.observeSchedule(studentNo).collect { list ->
                _cells.value = list
            }
        }
    }
}
