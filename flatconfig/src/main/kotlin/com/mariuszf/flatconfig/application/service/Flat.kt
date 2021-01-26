package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.SurfaceIsInvalidException
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

    fun validateState(): Flat = validateCommonPartSurfaceSurface().validateTotalSurface()

    private fun validateTotalSurface(): Flat = try {
        assert(totalSurface >= 0)
        this
    } catch (_: AssertionError) {
        throw SurfaceIsInvalidException("totalSurface for flat $id has negative value. Action aborted")
    }

    private fun validateCommonPartSurfaceSurface(): Flat = try {
        assert(commonPartSurface >= 0)
        this
    } catch (_: AssertionError) {
        throw SurfaceIsInvalidException("commonPartSurface for flat $id has negative value. Action aborted")
    }
}