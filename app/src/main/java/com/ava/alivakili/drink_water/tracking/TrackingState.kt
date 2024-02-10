package com.ava.alivakili.drink_water.tracking

import com.ava.alivakili.drink_water.WaterAmountModel

sealed class TrackingState {
    object Loading: TrackingState()
    data class WaterAmountList(val tasks:List<WaterAmountModel>): TrackingState()
}