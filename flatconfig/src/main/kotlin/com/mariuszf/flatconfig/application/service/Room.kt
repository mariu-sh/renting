package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.RoomSurfaceIsNegativeException
import java.util.*

class Room(val id: UUID, val surface: Double, val flatId: UUID) {
    init {
        validateState()
    }

    fun validateState(): Room = try {
        assert(surface >= 0)
        this
    } catch (_: AssertionError) {
        throw RoomSurfaceIsNegativeException("Surface for room $id is negative")
    }
}
