package com.example.unitconverter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unitconverter.R

class WeightViewModel : ViewModel() {
    private val _unit: MutableLiveData<Int> = MutableLiveData(R.string.kg)

    val unit: LiveData<Int>
        get() = _unit

    fun setUnit(value: Int) {

        _unit.value = value
    }

    private val _weight: MutableLiveData<String> = MutableLiveData("")

    val weight: LiveData<String>
        get() = _weight

    fun getWeightAsFloat(): Float = (_weight.value ?: "").let {
        return try {
            it.toFloat()
        } catch (e: NumberFormatException) {
            Float.NaN
        }
    }

    fun setWeight(value: String) {
        _weight.value = value
    }

    fun convert() = getWeightAsFloat().let {
        if (!it.isNaN())
            if (_unit.value == R.string.kg)
                it * 2.20462262F
            else
                it / 2.20462262F
        else
            Float.NaN
    }
}