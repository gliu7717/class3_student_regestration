package com.example.studentregister.db

import androidx.lifecycle.LiveData

interface StudentDao {
    suspend fun insertStudent(student: Student)

    suspend fun updateStudent(student: Student)

    suspend fun deleteStudent(student: Student)

    fun getAllStudents(): List<Student>
}