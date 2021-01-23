package com.mariuszf.flatconfig.application.port.out

import com.mariuszf.flatconfig.application.service.Room
import java.util.*

interface RoomStorage {
    fun findRoomById(roomId: UUID): Room
    fun findRoomsForFlatById(flatId: UUID): List<Room>
    fun createRoom(surface: Double, flatId: UUID): Room
    fun updateRoom(roomId: UUID, surface: Double): Room
    fun deleteRoom(roomId: UUID)
}