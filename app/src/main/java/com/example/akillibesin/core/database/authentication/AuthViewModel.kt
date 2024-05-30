package com.example.akillibesin.core.database.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    val mutableIsAuthComplete = MutableLiveData<Boolean>()

}