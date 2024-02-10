package com.ava.alivakili.drink_water.history

import com.ava.alivakili.drink_water.WaterAmountModel

sealed class HistoryState {
    object Loading:HistoryState()
    data class WaterAmountList(val amounts:List<WaterAmountModel>):HistoryState()

}