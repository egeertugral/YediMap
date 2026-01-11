package com.example.yedimap.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yedimap.data.room.entity.CourseEntity

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CourseEntity>): List<Long>

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun count(): Int
}
