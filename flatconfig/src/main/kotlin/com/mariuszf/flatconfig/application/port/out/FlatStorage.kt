package com.mariuszf.flatconfig.application.port.out

import com.mariuszf.flatconfig.application.service.Flat
import java.util.*

interface FlatStorage {
    fun createFlat(totalSurface: Double): Flat
    fun updateFlat(flatId: UUID, totalSurface: Double): Flat
    fun findFlatById(flatId: UUID): Flat
    fun findAllFlats(): List<Flat>
    fun deleteFlatById(flatId: UUID)
}

