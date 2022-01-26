package com.solvabit.phishingsmsdetector.screens.home

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.MainActivity
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val context: Context, private val cursor: Cursor): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context, cursor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}