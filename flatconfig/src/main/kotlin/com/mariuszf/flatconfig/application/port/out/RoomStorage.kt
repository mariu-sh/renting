package com.mariuszf.flatconfig.application.port.out

import com.mariuszf.flatconfig.application.service.Room
import java.util.*

interface RoomStorage {
    fun createRoom(surface: Double, flatId: UUID): Room
    fun findAllRooms(): List<Room>
    fun findRoomById(roomId: UUID): Room
    fun updateRoom(roomId: UUID, surface: Double): Room
    fun deleteRoom(roomId: UUID)
    fun findRoomsForFlatById(flatId: UUID): List<Room>

}