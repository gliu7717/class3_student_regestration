package com.example.studentregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentregister.db.Student
import com.example.studentregister.db.StudentDao
import com.example.studentregister.db.StudentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class StudentViewModel(private val dao: StudentDatabase,
                       val callback: ()-> Unit): ViewModel() {
    var students = MutableLiveData<List<Student>> ()
    init{
        var initialized = false;
        /*
        val deferedStudents = viewModelScope.async (Dispatchers.IO) {
            getStudents()
        }
        runBlocking {
            val lstStudents = deferedStudents.await()
            if (deferedStudents != null) {
                val searchStudentLiveData = MutableLiveData<List<Student>>()
                searchStudentLiveData.postValue(lstStudents)
                students = searchStudentLiveData
            }
        }
        */


        viewModelScope.launch (Dispatchers.IO) {
            val allStudents = dao.getAllStudents()
            if (allStudents != null) {
                students.postValue(allStudents)
            }
            initialized = true
            withContext(Dispatchers.Main)
            {
                callback()
            }
        }
/*        while(!initialized)
        {
            sleep(100)
        }
*/
    }

    suspend fun getStudents(): List<Student>
    {
        return dao.getAllStudents()
    }

    fun insertStudent(student: Student)
    {
        var lstStudent = students.value
        var listofStudents = mutableListOf<Student>()
        if (lstStudent != null) {
            listofStudents.addAll(lstStudent)
        }
        listofStudents.add(student)
        students.postValue(listofStudents)
        viewModelScope.launch (Dispatchers.IO) {
            dao.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        var lstStudent = students.value
        var listofStudents = mutableListOf<Student>()
        if (lstStudent != null) {
            listofStudents.addAll(lstStudent)
        }
        var idx = -1
        for (i in listofStudents.indices) {
            if(listofStudents[i].id == student.id)
            {
                idx = i
                break
            }
        }
        if(idx >=0) {
            listofStudents.removeAt(idx)
            listofStudents.add(student)
        }
        students.postValue(listofStudents)

        viewModelScope.launch(Dispatchers.IO) {
            dao.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student)
    {
        var lstStudent = students.value
        var listofStudents = mutableListOf<Student>()
        if (lstStudent != null) {
            listofStudents.addAll(lstStudent)
        }
        listofStudents.remove(student)
        students.postValue(listofStudents)
        viewModelScope.launch (Dispatchers.IO) {
            dao.deleteStudent(student)
        }
    }
}