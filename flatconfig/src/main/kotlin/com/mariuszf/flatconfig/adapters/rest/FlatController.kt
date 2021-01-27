package com.mariuszf.flatconfig.adapters.rest

import com.mariuszf.flatconfig.application.exceptions.FlatNotFoundException
import com.mariuszf.flatconfig.application.exceptions.SurfaceIsInvalidException
import com.mariuszf.flatconfig.application.port.`in`.ConfigureFlatUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/flat")
class FlatController(
    val configureFlatUseCase: ConfigureFlatUseCase
) {
    @GetMapping("/{flatId}")
    fun getFlat(@PathVariable flatId: UUID): FlatDTO =
        try {
            FlatDTO.fromDomain(configureFlatUseCase.getFlat(flatId))
        } catch (e: FlatNotFoundException) {
            throw FlatNotFoundRestException(e.message)
        }

    @GetMapping
    fun getAllFlats(): List<FlatDTO> =
        configureFlatUseCase.getAllFlats().map { FlatDTO.fromDomain(it) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFlat(@RequestBody flatCreateDTO: FlatCreateDTO): FlatDTO =
        try {
            FlatDTO.fromDomain(configureFlatUseCase.createFlat(flatCreateDTO.totalSurface))
        } catch (e: SurfaceIsInvalidException) {
            throw SurfaceIsInvalidRestException(e.message)
        }

    @PutMapping
    fun updateFlat(@RequestBody flatUpdateDTO: FlatUpdateDTO): FlatDTO =
        try {
            FlatDTO.fromDomain(configureFlatUseCase.updateFlat(flatUpdateDTO.flatId, flatUpdateDTO.totalSurface))
        } catch (e: FlatNotFoundException) {
            throw FlatNotFoundRestException(e.message)
        } catch (e: SurfaceIsInvalidException) {
            throw SurfaceIsInvalidRestException(e.message)
        }

    @DeleteMapping
    fun deleteFlat(@RequestBody flatDeleteDTO: FlatDeleteDTO) =
        try {
            configureFlatUseCase.deleteFlat(flatDeleteDTO.flatId)
        } catch (e: FlatNotFoundException) {
            throw FlatNotFoundRestException(e.message)
        }
}