package com.example.akillibesin.frontend.factory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NutritionViewModel : ViewModel() {
    val mutableNutrition = MutableLiveData<ArrayList<Float>>()
}