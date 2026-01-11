package com.example.yedimap.data.room.seed

import com.example.yedimap.data.room.db.AppDatabase
import com.example.yedimap.data.room.entity.*

object SeedData {

    suspend fun seedIfNeeded(db: AppDatabase) {
        // Çok basit kontrol: courses boşsa seed bas
        if (db.courseDao().count() > 0) return

        // Students (fake)
        val s1 = StudentEntity(studentNo = "20221308019", fullName = "Ege Ertuğral", email = "ege.ertugral@std.yeditepe.edu.tr", faculty = "Faculty of Computer and Information Sciences")
        val s2 = StudentEntity(studentNo = "2023123", fullName = "Kutay Büyükboyacı", email = "kutay.buyukboyaci@std.yeditepe.edu.tr", faculty = "Engineering")

        val s1Id = db.studentDao().upsert(s1)
        val s2Id = db.studentDao().upsert(s2)

        // Courses
        val courseIds = db.courseDao().insertAll(
            listOf(
                CourseEntity(code = "VCD404", name = "Visual Comm Design"),
                CourseEntity(code = "VCD422", name = "Typography"),
                CourseEntity(code = "RP418", name = "Research Project"),
                CourseEntity(code = "ART122", name = "Art History"),
                CourseEntity(code = "ART311", name = "Studio Practice")
            )
        )

        val (vcd404, vcd422, rp418, art122, art311) = courseIds

        // Schedule slots (Mon-Fri => 1..5) hours: 9-11, 11-13, 13-15, 15-17
        val slots = listOf(
            // Mon
            ScheduleSlotEntity(courseId = vcd404, dayOfWeek = 1, startHour = 9, endHour = 10, location = "gsf-7E03"),
            ScheduleSlotEntity(courseId = vcd404, dayOfWeek = 1, startHour = 10, endHour = 11, location = "gsf-7E03"),
            ScheduleSlotEntity(courseId = vcd422, dayOfWeek = 1, startHour = 16, endHour = 17, location = "gsf-105"),

            // Tue
            ScheduleSlotEntity(courseId = vcd404, dayOfWeek = 2, startHour = 11, endHour = 12, location = "gsf-7E03"),
            ScheduleSlotEntity(courseId = vcd422, dayOfWeek = 2, startHour = 15, endHour = 16, location = "gsf-105"),
            ScheduleSlotEntity(courseId = vcd422, dayOfWeek = 2, startHour = 16, endHour = 17, location = "gsf-105"),


            // Wed
            ScheduleSlotEntity(courseId = rp418, dayOfWeek = 3, startHour = 17, endHour = 18, location = "gsf-218"),

            // Thu
            ScheduleSlotEntity(courseId = art122, dayOfWeek = 4, startHour = 10, endHour = 11, location = "gsf-151"),
            ScheduleSlotEntity(courseId = art122, dayOfWeek = 4, startHour = 11, endHour = 12, location = "gsf-151"),

            ScheduleSlotEntity(courseId = art311, dayOfWeek = 4, startHour = 15, endHour = 16, location = "gsf-411"),
            ScheduleSlotEntity(courseId = art311, dayOfWeek = 4, startHour = 16, endHour = 17, location = "gsf-411")

        )

        db.scheduleDao().insertAll(slots)

        // Enrollments (s1: VCD404+VCD422+RP418, s2: ART122+ART311)
        db.enrollmentDao().insertAll(
            listOf(
                EnrollmentEntity(studentId = s1Id, courseId = vcd404),
                EnrollmentEntity(studentId = s1Id, courseId = vcd422),
                EnrollmentEntity(studentId = s1Id, courseId = rp418),
                EnrollmentEntity(studentId = s1Id, courseId = art122),
                EnrollmentEntity(studentId = s1Id, courseId = art311),


                EnrollmentEntity(studentId = s2Id, courseId = art122),
                EnrollmentEntity(studentId = s2Id, courseId = art311)
            )
        )
    }
}
