package com.ava.alivakili.drink_water.repository.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TOTAL_WATER_PREFERENCES_NAME="total_water_preferences"
private val Context.datastore by preferencesDataStore(name= TOTAL_WATER_PREFERENCES_NAME)
class WaterAmountRepository(context: Context) {
    private object Key{
        val TOTAL_WATER_KEY= stringPreferencesKey("TOTAL_WATER_KEY")
        val DRINKED_KEY= stringPreferencesKey("DRINKED_KEY")
        val PERCENTAGE_KEY= stringPreferencesKey("PERCENTAGE_KEY")
    }

    private val dataSource=context.datastore

    suspend fun get(): DataRestore?{
        return dataSource.data.catch {
            emit(emptyPreferences())
        }.map {preferences->
            val totalWater:Int= preferences[Key.TOTAL_WATER_KEY]?.toInt() ?:0
            val drinkedWater:Int=preferences[Key.DRINKED_KEY]?.toInt() ?:0
            val percentage:Double=preferences[Key.PERCENTAGE_KEY]?.toDouble()?:0.0
            return@map if(totalWater==0){
                null
            }else{
                DataRestore(totalWater,drinkedWater, percentage )
            }

        }.first()
    }

    suspend fun put(totalWater: Int,drinkedWater:Int,percentage: Double):Boolean{
        val result= kotlin.runCatching {
            dataSource.edit {preferences->
                preferences[Key.PERCENTAGE_KEY]=percentage.toString()
                preferences[Key.TOTAL_WATER_KEY]=totalWater.toString()
                preferences[Key.DRINKED_KEY]= drinkedWater.toString()
            }
        }
        return result.isSuccess
    }

    suspend fun deleteData(): Boolean {
        return try {
            dataSource.edit { preferences ->
                preferences.clear()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}