package com.mariuszf.flatconfig.application.port.out

import com.mariuszf.flatconfig.application.service.Flat
import java.util.*

interface FlatStorage {
    fun findFlatById(flatId: UUID): Flat
    fun createFlat(totalSurface: Double): Flat
    fun updateFlat(flatId: UUID, totalSurface: Double): Flat
    fun deleteFlatById(flatId: UUID)
}

