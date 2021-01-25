package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.CommonPartIsNegativeException
import java.util.*

class Flat(val id: UUID, val totalSurface: Double) {
    var rooms: List<Room> = emptyList()
    var commonPartSurface = totalSurface

    init {
        validateState()
    }

    fun updateCommonPartSurface() {
        commonPartSurface = totalSurface - sumRoomsSurface()
    }

    private fun sumRoomsSurface(): Double = rooms.sumByDouble { it.surface }

    fun validateState(): Flat = try {
        assert(commonPartSurface >= 0)
        this
    } catch (_: AssertionError) {
        throw CommonPartIsNegativeException("Common part for flat $id has negative value. Action aborted")
    }
}