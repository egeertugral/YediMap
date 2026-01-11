package com.example.yedimap.data.repository

import android.content.Context
import com.example.yedimap.data.room.db.AppDatabase
import com.example.yedimap.data.room.entity.ScheduleCell
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(context: Context) {
    private val db = AppDatabase.get(context)
    private val enrollmentDao = db.enrollmentDao()

    fun observeSchedule(studentNo: String): Flow<List<ScheduleCell>> {
        return enrollmentDao.getScheduleForStudent(studentNo)
    }
}
