package com.solvabit.phishingsmsdetector.screens.home

import android.content.ContentResolver
import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.MainActivity
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val cursor: Cursor): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(cursor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}