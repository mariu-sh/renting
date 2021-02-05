package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.adapters.postgres.FlatNotFoundInStorageException
import com.mariuszf.flatconfig.adapters.postgres.RoomNotFoundInStorageException
import com.mariuszf.flatconfig.application.exceptions.FlatNotFoundException
import com.mariuszf.flatconfig.application.exceptions.RoomNotFoundException
import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import com.mariuszf.flatconfig.application.port.out.FlatConfigInfoSync
import com.mariuszf.flatconfig.application.port.out.FlatStorage
import com.mariuszf.flatconfig.application.port.out.RoomStorage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class FlatConfigService(
    val flatStorage: FlatStorage,
    val roomStorage: RoomStorage,
    val flatConfigInfoSync: FlatConfigInfoSync
) : ConfigureFlatUseCase {

    override fun createFlat(totalSurface: Double): Flat {
        val flat: Flat = flatStorage.createFlat(totalSurface)
        flatConfigInfoSync.sendCreateCommand(flat)
        return flat
    }

    @Transactional
    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat {
        try {
            val flat = flatStorage.updateFlat(flatId, totalSurface).updateProperties().validateState()
            flatConfigInfoSync.sendUpdateCommand(flat)
            return flat
        } catch (e: FlatNotFoundInStorageException) { throw FlatNotFoundException(e.message) }
    }

    override fun getFlat(flatId: UUID): Flat =
        try { flatStorage.findFlatById(flatId).updateProperties() }
        catch (e: FlatNotFoundInStorageException) { throw FlatNotFoundException(e.message) }

    override fun getAllFlats(): List<Flat> = flatStorage.findAllFlats().map { it.updateProperties() }

    @Transactional
    override fun deleteFlat(flatId: UUID) =
        try {
            roomStorage.findRoomsForFlatById(flatId).forEach { deleteRoom(it.id) }
            flatStorage.deleteFlatById(flatId)
            flatConfigInfoSync.sendDeleteCommand(flatId)
        } catch (e: FlatNotFoundInStorageException) { throw FlatNotFoundException(e.message) }

    @Transactional
    override fun createRoom(surface: Double, flatId: UUID): Room {
        val room = roomStorage.createRoom(surface, getFlat(flatId).id).validateFlatState()
        flatConfigInfoSync.sendCreateCommand(room)
        return room
    }

    override fun getRoom(roomId: UUID): Room =
        try { roomStorage.findRoomById(roomId) }
        catch (e: RoomNotFoundInStorageException) { throw RoomNotFoundException(e.message) }

    override fun getAllRooms(): List<Room> = roomStorage.findAllRooms()

    private fun getRoomsForFlat(flatId: UUID): List<Room> = roomStorage.findRoomsForFlatById(flatId)

    @Transactional
    override fun updateRoom(roomId: UUID, surface: Double): Room {
        try {
            val room = roomStorage.updateRoom(roomId, surface).validateFlatState()
            flatConfigInfoSync.sendUpdateCommand(room)
            return room
        } catch (e: RoomNotFoundInStorageException) { throw RoomNotFoundException(e.message) }
    }

    override fun deleteRoom(roomId: UUID) =
        try {
            roomStorage.deleteRoom(getRoom(roomId).id)
            flatConfigInfoSync.sendDeleteCommand(roomId)
        } catch (e: RoomNotFoundInStorageException) {
            throw RoomNotFoundException(e.message)
        }

    private fun Flat.updateProperties(): Flat {
        rooms = getRoomsForFlat(id)
        updateCommonPartSurface()
        return this
    }

    private fun Room.validateFlatState(): Room {
        flatStorage.findFlatById(flatId).updateProperties().validateState()
        return this
    }
}