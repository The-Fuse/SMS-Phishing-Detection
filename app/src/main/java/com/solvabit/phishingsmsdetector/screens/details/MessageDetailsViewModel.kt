package com.solvabit.phishingsmsdetector.screens.details

import androidx.lifecycle.ViewModel
import com.solvabit.phishingsmsdetector.models.Message

class MessageDetailsViewModel(val message: Message): ViewModel() {

    init {
        initializeTranslation()
    }

    private fun initializeTranslation() {
        TODO("Not yet implemented")
    }
}