package com.example.viewmodelsms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelSMSManager : ViewModel() {

    private val _contactNumber = MutableStateFlow("")
    val contactNumber = _contactNumber.asStateFlow()

    private val _messageContent = MutableStateFlow("")
    val messageContent = _messageContent.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _screenCount = 3
    val screenCount: Int = _screenCount


    fun UpdateCurrentPage(newValue : Int){
        _currentPage.value = newValue
    }

    fun UpdateContactNumber(newValue : String){
        _contactNumber.value = newValue
    }

    fun UpdateMessageContent(newValue : String){
        _messageContent.value = newValue
    }

}