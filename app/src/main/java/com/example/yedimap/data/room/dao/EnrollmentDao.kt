package com.example.yedimap.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yedimap.data.room.entity.EnrollmentEntity
import com.example.yedimap.data.room.entity.ScheduleCell
import kotlinx.coroutines.flow.Flow

@Dao
interface EnrollmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<EnrollmentEntity>)

    @Query("SELECT COUNT(*) FROM enrollments")
    suspend fun count(): Int

    // StudentNo -> Enrollments -> Course -> Schedule
    @Query("""
        SELECT 
            s.dayOfWeek as dayOfWeek,
            s.startHour as startHour,
            s.endHour as endHour,
            c.code as courseCode,
            s.location as location
        FROM students st
        INNER JOIN enrollments e ON e.studentId = st.id
        INNER JOIN courses c ON c.id = e.courseId
        INNER JOIN course_schedule s ON s.courseId = c.id
        WHERE st.studentNo = :studentNo
    """)
    fun getScheduleForStudent(studentNo: String): Flow<List<ScheduleCell>>
}
