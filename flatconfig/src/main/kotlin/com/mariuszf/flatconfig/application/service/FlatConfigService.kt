package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.adapters.postgres.FlatNotFoundInStorageException
import com.mariuszf.flatconfig.adapters.postgres.RoomNotFoundInStorageException
import com.mariuszf.flatconfig.application.exceptions.FlatNotFoundException
import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import com.mariuszf.flatconfig.application.port.out.FlatStorage
import com.mariuszf.flatconfig.application.port.out.RoomStorage
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class FlatConfigService(val flatStorage: FlatStorage, val roomStorage: RoomStorage) : ConfigureFlatUseCase {

    override fun createFlat(totalSurface: Double): Flat = flatStorage.createFlat(totalSurface)

    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat =
        try {
            flatStorage.updateFlat(flatId, totalSurface).updateProperties().validateState()
        } catch (e: FlatNotFoundInStorageException) {
            throw FlatNotFoundException(e.message)
        }

    override fun getFlat(flatId: UUID): Flat =
        try {
            flatStorage.findFlatById(flatId).updateProperties()
        } catch (e: FlatNotFoundInStorageException) {
            throw FlatNotFoundException(e.message)
        }

    override fun getAllFlats(): List<Flat> = flatStorage.findAllFlats().map { it.updateProperties() }

    @Transactional
    override fun deleteFlat(flatId: UUID) =
        try {
            roomStorage.findRoomsForFlatById(flatId).forEach { roomStorage.deleteRoom(it.id) }
            flatStorage.deleteFlatById(flatId)
        } catch (e: FlatNotFoundInStorageException) {
            throw FlatNotFoundException(e.message)
        }

    @Transactional
    override fun createRoom(surface: Double, flatId: UUID): Room  =
        roomStorage.createRoom(surface, flatId).validateFlatState()

    override fun getRoom(roomId: UUID): Room =
        try {
            roomStorage.findRoomById(roomId)
        } catch (e: RoomNotFoundInStorageException) {
            throw RoomNotFoundInStorageException(e.message)
        }

    override fun getAllRooms(): List<Room> = roomStorage.findAllRooms()

    private fun getRoomsForFlat(flatId: UUID): List<Room> = roomStorage.findRoomsForFlatById(flatId)

    @Transactional
    override fun updateRoom(roomId: UUID, surface: Double): Room =
        try {
            roomStorage.updateRoom(roomId, surface).validateFlatState()
        } catch (e: RoomNotFoundInStorageException) {
            throw RoomNotFoundInStorageException(e.message)
        }

    override fun deleteRoom(roomId: UUID) =
        try {
            roomStorage.deleteRoom(roomId)
        } catch (e: RoomNotFoundInStorageException) {
            throw RoomNotFoundInStorageException(e.message)
        }

    private fun Flat.updateProperties(): Flat = run {
        rooms = getRoomsForFlat(id)
        updateCommonPartSurface()
        return this
    }

    private fun Room.validateFlatState(): Room {
        flatStorage.findFlatById(flatId).updateProperties().validateState()
        return this
    }
}