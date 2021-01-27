package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.SurfaceIsInvalidException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID.randomUUID
import kotlin.random.Random

class FlatRoomTest : FlatConfigServiceTestBase() {

    private val totalSurface = Random.nextDouble() * 100
    private val roomSurfaceList = listOf(totalSurface / 3, totalSurface / 5, totalSurface / 6)
    private val flat = Flat(randomUUID(), totalSurface)
    private val rooms = roomSurfaceList.map { Room(randomUUID(), it, flat.id) }

    @Test
    fun createFlatAndRooms() {
        //WHEN
        val flat = flatConfigService.createFlat(totalSurface)
        roomSurfaceList.forEach { flatConfigService.createRoom(it, flat.id) }

        //THEN
        assertThat(flat.commonPartSurface).isEqualTo(totalSurface - roomSurfaceList.sumByDouble { it })
        assertThat(roomStorage.storage).hasSize(3)
        assertThat(flat.rooms).hasSize(3)
        assertThat(flat.rooms.map { it.surface }).containsExactlyInAnyOrder(*roomSurfaceList.toTypedArray())
    }

    @Test
    fun updateFlatWithRooms() {
        //GIVEN
        setupFlatAndRooms()
        val newTotalSurface = totalSurface * 2

        //WHEN
        flatConfigService.updateFlat(flat.id, newTotalSurface)

        //THEN
        assertThat(flatStorage.storage.first().commonPartSurface)
            .isEqualTo(newTotalSurface - roomSurfaceList.sumByDouble { it })
        assertThat(flatStorage.storage.first().rooms).hasSize(3)
        assertThat(flatStorage.storage.first().rooms.map { it.surface })
            .containsExactlyInAnyOrder(*roomSurfaceList.toTypedArray())
    }

    @Test
    fun updateRoom_forFlatWithRoom() {
        //GIVEN
        setupFlatAndRooms()
        val roomToUpdateId = roomStorage.storage.first().id
        val newRoomSurface = totalSurface / 2
        val flatCommonSurfaceBeforeUpdate = flatStorage.storage.first().commonPartSurface

        //WHEN
        flatConfigService.updateRoom(roomToUpdateId, newRoomSurface)

        //THEN
        assertThat(flat.commonPartSurface).isNotEqualTo(flatCommonSurfaceBeforeUpdate)
        assertThat(flat.rooms.find { it.surface == newRoomSurface }).isNotNull
    }

    @Test
    fun failUpdating_whenRoomSurfaceTooBig(){
        //GIVEN
        setupFlatAndRooms()

        //EXCEPT
        assertThrows<SurfaceIsInvalidException> {
            flatConfigService.updateRoom(roomStorage.storage.first().id, totalSurface)
        }
    }

    @Test
    fun deleteFlatWithAllRooms() {
        //GIVEN
        setupFlatAndRooms()

        //WHEN
        flatConfigService.deleteFlat(flat.id)

        //THEN
        assertThat(flatStorage.storage.find { it.id == flat.id }).isNull()
        assertThat(roomStorage.storage.filter { it.flatId == flat.id }).isEmpty()
    }

    private fun setupFlatAndRooms() {
        flatStorage.storage.add(flat)
        rooms.forEach { roomStorage.storage.add(it) }
    }

}