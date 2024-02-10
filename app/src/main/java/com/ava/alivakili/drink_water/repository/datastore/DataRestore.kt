package com.ava.alivakili.drink_water.repository.datastore

data class DataRestore (
    val totalWater:Int,
    val drinkedWater:Int,
    val percentage:Double

){

    fun isValid():Boolean{
        return totalWater!=0
    }


}