package com.ava.alivakili.drink_water

import com.ava.alivakili.drink_water.repository.database.WaterAmountEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class WaterAmountModel (
    val amount:Int,
    val id:String,
    var time:String,
    val drinkedAmount:Int,
    val percentage: String
){

    constructor(waterAmountEntity: WaterAmountEntity) : this(
        id = waterAmountEntity.id,
        amount = waterAmountEntity.amount,
        time = waterAmountEntity.time,
        drinkedAmount = waterAmountEntity.drinkedAmount,
        percentage = waterAmountEntity.percentage
    )

    fun setTime(){
        val currentTime = Calendar.getInstance().time
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        time= format.format(currentTime)
    }

}
