package com.example.yedimap.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yedimap.data.room.entity.StudentEntity

@Dao
interface StudentDao {

    @Query("SELECT * FROM students WHERE studentNo = :studentNo LIMIT 1")
    suspend fun getByStudentNo(studentNo: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(student: StudentEntity): Long

    @Query("SELECT COUNT(*) FROM students")
    suspend fun count(): Int
}
