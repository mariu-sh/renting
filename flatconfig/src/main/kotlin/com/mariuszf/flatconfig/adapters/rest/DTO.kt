package com.mariuszf.flatconfig.adapters.rest

import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import org.jetbrains.annotations.NotNull
import java.util.*

data class FlatDTO(
    @NotNull val flatId: UUID,
    @NotNull val totalSurface: Double,
    @NotNull val commonPartSurface: Double,
    @NotNull val rooms: List<RoomSimpleDTO>
) {
    companion object {
        @JvmStatic
        fun fromDomain(flat: Flat): FlatDTO =
            FlatDTO(
                flat.id,
                flat.totalSurface,
                flat.commonPartSurface,
                flat.rooms.map { RoomSimpleDTO.fromDomain(it) }
            )
    }
}
data class FlatCreateDTO(val totalSurface: Double)
data class FlatUpdateDTO(val flatId: UUID, val totalSurface: Double)
data class FlatDeleteDTO(val flatId: UUID)

data class RoomDTO(val roomId: UUID, val surface: Double, val flatId: UUID) {
    companion object {
        @JvmStatic
        fun fromDomain(room: Room): RoomDTO = RoomDTO(room.id, room.surface, room.flatId)
    }
}

data class RoomSimpleDTO(val roomId: UUID, val surface: Double) {

    companion object {
        @JvmStatic
        fun fromDomain(room: Room): RoomSimpleDTO = RoomSimpleDTO(room.id, room.surface)
    }
}

data class RoomCreateDTO(val surface: Double, val flatId: UUID)
data class RoomUpdateDTO(val roomId: UUID, val surface: Double)
data class RoomDeleteDTO(val roomId: UUID)