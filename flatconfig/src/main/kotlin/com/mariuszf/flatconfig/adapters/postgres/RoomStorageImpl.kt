package com.mariuszf.flatconfig.adapters.postgres

import com.mariuszf.flatconfig.application.port.out.RoomStorage
import com.mariuszf.flatconfig.application.service.Room
import org.springframework.data.repository.CrudRepository
import java.lang.Exception
import java.util.*
import javax.persistence.*

interface RoomStorageImpl: RoomStorage, CrudRepository<RoomEntity, UUID> {

    override fun findRoomById(roomId: UUID): Room = Optional.of(findById(roomId))
            .map { it.get().toDomain() }
            .orElseThrow { Exception() }

    override fun findRoomsForFlatById(flatId: UUID): List<Room>  =
            findRoomEntitiesByFlatId(flatId).map { it.toDomain() }

    fun findRoomEntitiesByFlatId(flatId: UUID): List<RoomEntity>

    override fun createRoom(surface: Double, flatId: UUID): Room {
        val roomEntity = RoomEntity()
        roomEntity.surface = surface
        roomEntity.flatId = flatId
        return save(roomEntity).toDomain()
    }

    override fun updateRoom(roomId: UUID, surface: Double): Room {
        val roomEntity = findById(roomId).get()
        roomEntity.surface = surface
        return save(roomEntity).toDomain()
    }

    override fun deleteRoom(roomId: UUID) = deleteById(roomId)
}

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "room")
class RoomEntity{

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    lateinit var roomId: UUID

    @Column(name = "surface")
    var surface = 0.0

    @Column(name = "flat_id")
    lateinit var flatId: UUID

    fun toDomain() = Room(roomId, surface, flatId)
}