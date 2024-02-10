package com.ava.alivakili.drink_water.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.WaterAmountModel
import com.ava.alivakili.drink_water.repository.database.WaterAmountDao
import com.ava.alivakili.drink_water.repository.database.WaterAmountEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class TrackingViewModel(private val dao: WaterAmountDao) : ViewModel() {

    private val _state: MutableStateFlow<TrackingState> = MutableStateFlow(TrackingState.Loading)
    val state: StateFlow<TrackingState> = _state

    fun onWaterAmountCreated(amount: Int,totalAmount: Int,drinkedAmount:Int) {
        viewModelScope.launch {
            val amount = WaterAmountModel(
                id = UUID.randomUUID().toString(),
                amount = amount,
                time = getTime(),
                drinkedAmount = drinkedAmount,
                percentage = getPercentage(drinkedAmount, totalAmount)
            )
            dao.insert(WaterAmountEntity(amount ))

            val newList = dao.get().map ( ::WaterAmountModel )
            _state.value = TrackingState.WaterAmountList(newList)
        }
    }

    private fun getPercentage(drinkedAmount: Int,totalAmount:Int): String {
        return (drinkedAmount/totalAmount.toDouble()).toString()
    }

    private fun getTime(): String {
        val currentTime = Calendar.getInstance().time
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(currentTime)
    }



    companion object {
        fun factory(dao: WaterAmountDao): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackingViewModel(dao) as T
                }
            }
        }
    }
}
