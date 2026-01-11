package com.example.yedimap.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,        // VCD404
    val name: String         // Visual Comm Design
)
