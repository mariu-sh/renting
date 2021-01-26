package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.adapters.postgres.RoomNotFoundInStorageException
import com.mariuszf.flatconfig.application.port.out.RoomStorage
import java.util.*
import java.util.UUID.randomUUID
import kotlin.streams.toList

class InMemoryRoomStorage : RoomStorage{

    val storage = mutableListOf<Room>()

    override fun createRoom(surface: Double, flatId: UUID): Room {
        val room = Room(randomUUID(), surface, flatId)
        storage.add(room)
        return room
    }

    override fun findAllRooms(): List<Room> = storage

    override fun findRoomById(roomId: UUID): Room =
        storage.find { it.id == roomId } ?: throw RoomNotFoundInStorageException("")

    override fun updateRoom(roomId: UUID, surface: Double): Room {
        val room = findRoomById(roomId)
        val updatedRoom = Room(roomId, surface, room.flatId)
        storage.removeIf { it.id == roomId }
        storage.add(updatedRoom)
        return updatedRoom
    }

    override fun deleteRoom(roomId: UUID) {
        storage.removeIf { it.id == roomId }
    }

    override fun findRoomsForFlatById(flatId: UUID): List<Room> =
        storage.stream().filter { it.flatId == flatId }.toList()
}