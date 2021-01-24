package com.mariuszf.flatconfig.adapters.postgres

import com.mariuszf.flatconfig.application.port.out.RoomStorage
import com.mariuszf.flatconfig.application.service.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Component
class RoomStorageImpl(
    val postgresRoomStorage: PostgresRoomStorage
): RoomStorage {

    override fun createRoom(surface: Double, flatId: UUID): Room =
        postgresRoomStorage.save(RoomEntity(UUID.randomUUID(), surface, flatId)).toDomain()

    override fun findRoomById(roomId: UUID): Room = postgresRoomStorage.findById(roomId)
        .map { it.toDomain() }
        .orElseThrow { Exception() }

    override fun findAllRooms(): List<Room> = postgresRoomStorage.findAll().map { it.toDomain() }

    override fun updateRoom(roomId: UUID, surface: Double): Room {
        val roomEntity = postgresRoomStorage.findById(roomId).get()
        roomEntity.surface = surface
        return postgresRoomStorage.save(roomEntity).toDomain()
    }

    override fun deleteRoom(roomId: UUID) = postgresRoomStorage.deleteById(roomId)

    override fun findRoomsForFlatById(flatId: UUID): List<Room> =
        postgresRoomStorage.findRoomEntitiesByFlatId(flatId)
            .map { it.toDomain() }
}

@Repository
interface PostgresRoomStorage: JpaRepository<RoomEntity, UUID>{
    fun findRoomEntitiesByFlatId(flatId: UUID): List<RoomEntity>
}

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "room")
data class RoomEntity(
    @Id @Column(name = "room_id") val id: UUID?,
    @Column(name = "surface") var surface: Double,
    @Column(name = "flat_id") val flatId: UUID?
){
    constructor(): this(null, 0.0, null)
    fun toDomain(): Room = Room(id!!, surface, flatId!!)
}