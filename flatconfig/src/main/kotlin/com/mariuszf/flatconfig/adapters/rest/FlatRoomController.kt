package com.mariuszf.flatconfig.adapters.rest

import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")
class FlatRoomController(val configureFlatUseCase: ConfigureFlatUseCase) {

    @GetMapping("/flat/{flatId}")
    fun getFlat(@PathVariable flatId: UUID): FlatDTO = FlatDTO.fromDomain(configureFlatUseCase.getFlat(flatId))

    @GetMapping("/flat")
    fun getAllFlats(): List<FlatDTO> = configureFlatUseCase.getAllFlats().map { FlatDTO.fromDomain(it) }

    @PostMapping("/flat")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFlat(@RequestBody flatCreateDTO: FlatCreateDTO): FlatDTO =
        FlatDTO.fromDomain(configureFlatUseCase.createFlat(flatCreateDTO.totalSurface))

    @PutMapping("/flat")
    fun updateFlat(@RequestBody flatUpdateDTO: FlatUpdateDTO): FlatDTO =
        FlatDTO.fromDomain(configureFlatUseCase.updateFlat(flatUpdateDTO.id, flatUpdateDTO.totalSurface))

    @DeleteMapping("/flat")
    fun deleteFlat(@RequestBody flatDeleteDTO: FlatDeleteDTO) = configureFlatUseCase.deleteFlat(flatDeleteDTO.id)

    @GetMapping("/room/{roomId}")
    fun getRoom(@PathVariable roomId: UUID): RoomDTO = RoomDTO.fromDomain(configureFlatUseCase.getRoom(roomId))

    @GetMapping("/room")
    fun getAllRooms(): List<RoomDTO> = configureFlatUseCase.getAllRooms().map { RoomDTO.fromDomain(it) }

    @PostMapping("/room")
    fun createRoom(@RequestBody roomCreateDTO: RoomCreateDTO): RoomDTO =
        RoomDTO.fromDomain(configureFlatUseCase.createRoom(roomCreateDTO.surface, roomCreateDTO.flatId))

    @PutMapping("/room")
    fun updateRoom(@RequestBody roomUpdateDTO: RoomUpdateDTO): RoomDTO =
        RoomDTO.fromDomain(configureFlatUseCase.updateRoom(roomUpdateDTO.id, roomUpdateDTO.surface))

    @DeleteMapping("/room")
    fun deleteRoom(@RequestBody roomDeleteDTO: RoomDeleteDTO) = configureFlatUseCase.deleteRoom(roomDeleteDTO.id)

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
    data class FlatUpdateDTO(val id: UUID, val totalSurface: Double)
    data class FlatDeleteDTO(val id: UUID)

    data class RoomSimpleDTO(val id: UUID, val surface: Double) {
        companion object {
            @JvmStatic
            fun fromDomain(room: Room): RoomSimpleDTO = RoomSimpleDTO(room.id, room.surface)
        }
    }

    data class RoomDTO(val id: UUID, val surface: Double, val flatId: UUID) {
        companion object {
            @JvmStatic
            fun fromDomain(room: Room): RoomDTO = RoomDTO(room.id, room.surface, room.flatId)
        }
    }

    data class RoomCreateDTO(val surface: Double, val flatId: UUID)
    data class RoomUpdateDTO(val id: UUID, val surface: Double)
    data class RoomDeleteDTO(val id: UUID)

}