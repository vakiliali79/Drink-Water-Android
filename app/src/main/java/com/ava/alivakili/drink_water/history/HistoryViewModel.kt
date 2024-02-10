package com.ava.alivakili.drink_water.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.WaterAmountModel
import com.ava.alivakili.drink_water.repository.database.WaterAmountDao

class HistoryViewModel (private val dao: WaterAmountDao) : ViewModel() {

    private val _state: MutableStateFlow<HistoryState> = MutableStateFlow(HistoryState.Loading)
    val state: StateFlow<HistoryState> = _state

    init {
        retrieveAmounts()
    }

    private fun retrieveAmounts() {
        viewModelScope.launch {
            val amounts = dao.get().map ( ::WaterAmountModel )
            _state.value = HistoryState.WaterAmountList(amounts)
        }
    }



    companion object {
        fun factory(dao: WaterAmountDao): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(dao) as T
                }
            }
        }
    }
}
