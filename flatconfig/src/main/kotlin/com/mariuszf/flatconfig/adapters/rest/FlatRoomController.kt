package com.mariuszf.flatconfig.adapters.rest

import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/v1"])
class FlatRoomController(val configureFlatUseCase: ConfigureFlatUseCase) {

    @GetMapping("/flat/{flatId}")
    fun getFlat(@PathVariable flatId: UUID): FlatDTO = FlatDTO.fromDomain(configureFlatUseCase.getFlat(flatId))

    @PostMapping("/flat")
    fun createFlat(flatCreateDTO: FlatCreateDTO): FlatDTO =
        FlatDTO.fromDomain(configureFlatUseCase.createFlat(flatCreateDTO.totalSurface))

    @PutMapping("/flat")
    fun updateFlat(flatUpdateDTO: FlatUpdateDTO): FlatDTO =
        FlatDTO.fromDomain(configureFlatUseCase.updateFlat(flatUpdateDTO.id, flatUpdateDTO.totalSurface))

    @DeleteMapping("/flat")
    fun deleteFlat(flatDeleteDTO: FlatDeleteDTO) = configureFlatUseCase.deleteFlat(flatDeleteDTO.id)

    @GetMapping("/room/{roomId}")
    fun getRoom(@PathVariable roomId: UUID): RoomDTO = RoomDTO.fromDomain(configureFlatUseCase.getRoom(roomId))

    @PostMapping("/room")
    fun createRoom(roomCreateDTO: RoomCreateDTO): RoomDTO =
        RoomDTO.fromDomain(configureFlatUseCase.createRoom(roomCreateDTO.surface, roomCreateDTO.flatId))

    @PutMapping("/room")
    fun updateRoom(roomUpdateDTO: RoomUpdateDTO): RoomDTO =
        RoomDTO.fromDomain(configureFlatUseCase.updateRoom(roomUpdateDTO.id, roomUpdateDTO.surface))

    @DeleteMapping("/room")
    fun deleteRoom(roomDeleteDTO: RoomDeleteDTO) = configureFlatUseCase.deleteRoom(roomDeleteDTO.id)

    data class FlatDTO(@NotNull val flatId: UUID, @NotNull val totalSurface: Double, @NotNull val rooms: List<RoomSimpleDTO>) {
        companion object {
            @JvmStatic
            fun fromDomain(flat: Flat): FlatDTO =
                FlatDTO(flat.id, flat.totalSurface, flat.rooms.map { RoomSimpleDTO.fromDomain(it) })
        }
    }

    data class FlatCreateDTO(@NotNull val totalSurface: Double)
    data class FlatUpdateDTO(@NotNull val id: UUID, @NotNull val totalSurface: Double)
    data class FlatDeleteDTO(@NotNull val id: UUID)

    data class RoomSimpleDTO(@NotNull val id: UUID, @NotNull val surface: Double) {
        companion object {
            @JvmStatic
            fun fromDomain(room: Room): RoomSimpleDTO = RoomSimpleDTO(room.id, room.surface)
        }
    }

    data class RoomDTO(@NotNull val id: UUID, @NotNull val surface: Double, @NotNull val flatId: UUID) {
        companion object {
            @JvmStatic
            fun fromDomain(room: Room): RoomDTO = RoomDTO(room.id, room.surface, room.flatId)
        }
    }

    data class RoomCreateDTO(@NotNull val surface: Double, @NotNull val flatId: UUID)
    data class RoomUpdateDTO(@NotNull val id: UUID, @NotNull val surface: Double)
    data class RoomDeleteDTO(@NotNull val id: UUID)

}