package com.ava.alivakili.drink_water.goal


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.repository.datastore.WaterAmountRepository

class GoalViewModel (private val repository: WaterAmountRepository) : ViewModel(){

    private val _state: MutableStateFlow<GoalState> = MutableStateFlow(GoalState.Idle)
    val state: StateFlow<GoalState> =_state

    fun save(totalWater: Int, drinkedWater:Int, percentage: Double){
        viewModelScope.launch {
            if(totalWater!=0){
                val isSuccess=repository.put(totalWater, drinkedWater , percentage )
                _state.value=if(isSuccess)GoalState.SuccessSaving
                else GoalState.ErrorSaving
            }else{
                _state.value=GoalState.ZeroAmount
            }
        }
    }

    companion object{
        fun factory(repository: WaterAmountRepository): ViewModelProvider.Factory{
            return object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GoalViewModel(repository = repository)as T
                }
            }
        }
    }




}