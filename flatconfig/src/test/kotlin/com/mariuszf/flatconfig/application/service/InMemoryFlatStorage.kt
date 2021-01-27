package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.adapters.postgres.FlatNotFoundInStorageException
import com.mariuszf.flatconfig.application.port.out.FlatStorage
import java.util.*
import java.util.UUID.randomUUID

class InMemoryFlatStorage: FlatStorage {

    val storage = mutableListOf<Flat>()

    override fun createFlat(totalSurface: Double): Flat {
        val flat = Flat(randomUUID(), totalSurface)
        storage.add(flat)
        return flat
    }

    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat {
        val flat = findFlatById(flatId)
        val updatedFlat = Flat(flatId, totalSurface)
        storage.removeIf { it.id == flat.id }
        storage.add(updatedFlat)
        return updatedFlat
    }

    override fun findFlatById(flatId: UUID): Flat =
        storage.find { it.id == flatId } ?: throw FlatNotFoundInStorageException("")

    override fun findAllFlats(): List<Flat> = storage

    override fun deleteFlatById(flatId: UUID) {
        storage.remove(findFlatById(flatId))
    }
}