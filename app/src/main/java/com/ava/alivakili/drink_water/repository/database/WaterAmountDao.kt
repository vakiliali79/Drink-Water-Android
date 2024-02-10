package com.ava.alivakili.drink_water.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WaterAmountDao {

    @Query("SELECT * FROM waterAmount")
    suspend fun get():List<WaterAmountEntity>

    @Insert
    suspend fun insert(waterAmount:WaterAmountEntity)

    @Update
    suspend fun update(waterAmount: WaterAmountEntity)

    @Query("DELETE FROM waterAmount")
    suspend fun deleteAll()


}