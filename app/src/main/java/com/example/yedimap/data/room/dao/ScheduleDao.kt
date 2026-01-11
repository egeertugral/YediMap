package com.example.yedimap.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yedimap.data.room.entity.ScheduleSlotEntity

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ScheduleSlotEntity>)

    @Query("SELECT COUNT(*) FROM course_schedule")
    suspend fun count(): Int
}
