package com.ava.alivakili.drink_water.goal

sealed class GoalState {
    object Idle:GoalState()
    object ErrorSaving:GoalState()
    object ZeroAmount:GoalState()
    object SuccessSaving:GoalState()
}