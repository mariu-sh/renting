package com.mariuszf.flatconfig.adapters.rest

import com.mariuszf.flatconfig.application.exceptions.RoomNotFoundException
import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/room")
class RoomController(val configureFlatUseCase: ConfigureFlatUseCase) {

    @GetMapping("/{roomId}")
    fun getRoom(@PathVariable roomId: UUID): RoomDTO =
        try {
            RoomDTO.fromDomain(configureFlatUseCase.getRoom(roomId))
        } catch (e: RoomNotFoundException) {
            throw RoomNotFoundRestException(e.message)
        }

    @GetMapping
    fun getAllRooms(): List<RoomDTO> = configureFlatUseCase.getAllRooms().map { RoomDTO.fromDomain(it) }

    @PostMapping
    fun createRoom(@RequestBody roomCreateDTO: RoomCreateDTO): RoomDTO =
        RoomDTO.fromDomain(configureFlatUseCase.createRoom(roomCreateDTO.surface, roomCreateDTO.flatId))

    @PutMapping
    fun updateRoom(@RequestBody roomUpdateDTO: RoomUpdateDTO): RoomDTO =
        try {
            RoomDTO.fromDomain(configureFlatUseCase.updateRoom(roomUpdateDTO.roomId, roomUpdateDTO.surface))
        } catch (e: RoomNotFoundException) {
            throw RoomNotFoundRestException(e.message)
        }

    @DeleteMapping
    fun deleteRoom(@RequestBody roomDeleteDTO: RoomDeleteDTO) =
        try {
            configureFlatUseCase.deleteRoom(roomDeleteDTO.roomId)
        } catch (e: RoomNotFoundException) {
            throw RoomNotFoundRestException(e.message)
        }
}