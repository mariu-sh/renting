package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import com.mariuszf.flatconfig.application.port.out.FlatStorage
import com.mariuszf.flatconfig.application.port.out.RoomStorage
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class FlatConfigService(
        val flatStorage: FlatStorage,
        val roomStorage: RoomStorage
) : ConfigureFlatUseCase {

    override fun createFlat(totalSurface: Double): Flat = flatStorage.createFlat(totalSurface)

    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat = flatStorage.updateFlat(flatId, totalSurface)

    override fun getFlat(flatId: UUID): Flat = flatStorage.findFlatById(flatId)

    override fun getAllFlats(): List<Flat> = flatStorage.findAllFlats()

    @Transactional
    override fun deleteFlat(flatId: UUID){
        roomStorage.findRoomsForFlatById(flatId).forEach { roomStorage.deleteRoom(it.id) }
        flatStorage.deleteFlatById(flatId)
    }

    override fun createRoom(surface: Double, flatId: UUID): Room = roomStorage.createRoom(surface, flatId)

    override fun getRoom(roomId: UUID): Room = roomStorage.findRoomById(roomId)

    override fun getAllRooms(): List<Room> = roomStorage.findAllRooms()

    override fun updateRoom(roomId: UUID, surface: Double): Room = roomStorage.updateRoom(roomId, surface)

    override fun deleteRoom(roomId: UUID) = roomStorage.deleteRoom(roomId)

}