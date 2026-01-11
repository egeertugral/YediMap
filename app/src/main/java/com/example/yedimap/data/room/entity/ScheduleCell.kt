package com.example.yedimap.data.room.entity

data class ScheduleCell(
    val dayOfWeek: Int,
    val startHour: Int,
    val endHour: Int,
    val courseCode: String,
    val location: String
)
