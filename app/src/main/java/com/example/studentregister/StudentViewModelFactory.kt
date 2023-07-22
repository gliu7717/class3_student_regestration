package com.example.studentregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentregister.db.StudentDatabase
import java.lang.IllegalArgumentException

class StudentViewModelFactory(
    private val dao: StudentDatabase,
    val callback: ()-> Unit
):ViewModelProvider.Factory {
     override  fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentViewModel::class.java)){
            return StudentViewModel(dao, callback) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}