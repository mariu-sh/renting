package com.mariuszf.flatconfig.application.port.`in`

import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import java.util.*

interface ConfigureFlatUseCase {

    fun createFlat(totalSurface: Double): Flat
    fun getFlat(flatId: UUID): Flat
    fun getAllFlats(): List<Flat>
    fun updateFlat(flatId: UUID, totalSurface: Double): Flat
    fun deleteFlat(flatId: UUID)
    fun createRoom(surface: Double, flatId: UUID): Room
    fun getRoom(roomId: UUID): Room
    fun getAllRooms(): List<Room>
    fun updateRoom(roomId: UUID, surface: Double): Room
    fun deleteRoom(roomId: UUID)

}