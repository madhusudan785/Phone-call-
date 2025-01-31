package com.example.phonecall.viewModels


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialerViewModel : ViewModel() {
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    fun appendDigit(digit: String) {
        _phoneNumber.value += digit
    }

    fun removeLastDigit() {
        if (_phoneNumber.value.isNotEmpty()) {
            _phoneNumber.value = _phoneNumber.value.dropLast(1)
        }
    }

    fun clearNumber() {
        _phoneNumber.value = ""
    }

    fun setPhoneNumber(number: String) {
        _phoneNumber.value = number
    }


}
