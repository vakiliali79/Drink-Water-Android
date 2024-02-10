package com.ava.alivakili.drink_water.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ava.alivakili.drink_water.WaterAmountModel

@Entity(tableName = "waterAmount")
data class WaterAmountEntity (
    @PrimaryKey val id:String,
    @ColumnInfo(name = "amount") val amount:Int,
    @ColumnInfo(name = "time")val time:String,
    @ColumnInfo(name = "drinkedAmount")val drinkedAmount:Int,
    @ColumnInfo(name = "percentage")val percentage: String
){
    constructor(waterAmountModel: WaterAmountModel):this(
        id=waterAmountModel.id,
        amount=waterAmountModel.amount,
        time=waterAmountModel.time,
        drinkedAmount=waterAmountModel.drinkedAmount,
        percentage=waterAmountModel.percentage
    )
}