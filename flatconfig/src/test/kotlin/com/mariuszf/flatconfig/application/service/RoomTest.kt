package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.FlatNotFoundException
import com.mariuszf.flatconfig.application.exceptions.RoomNotFoundException
import com.mariuszf.flatconfig.application.exceptions.SurfaceIsInvalidException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import java.util.UUID.randomUUID
import kotlin.random.Random

class RoomTest : FlatConfigServiceTestBase() {

    @Test
    fun createRoom() {
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomSurface = totalSurface / 2

        //WHEN
        val room = flatConfigService.createRoom(roomSurface, flatId)

        //THEN
        assertThat(room.flatId).isEqualTo(flatId)
        assertThat(room.id).isInstanceOf(UUID::class.java)
        assertThat(room.surface).isEqualTo(roomSurface)
        assertThat(flat.rooms).hasSize(1)
        assertThat(flat.rooms.first()).isEqualTo(room)
    }

    @Test
    fun failCreatingRoom_withNegativeSurface() {
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomSurface = totalSurface.unaryMinus() / 2

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.createRoom(roomSurface, flatId) }
    }

    @Test
    fun failCreatingRoom_withSurfaceExceedingTotalSurface() {
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomSurface = totalSurface * 2

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.createRoom(roomSurface, flatId) }
    }

    @Test
    fun failCreatingRoom_whenFlatNotExisting() {
        //GIVEN
        val roomSurface = Random.nextDouble() * 100
        val flatId = randomUUID()

        //EXPECT
        assertThrows<FlatNotFoundException> { flatConfigService.createRoom(roomSurface, flatId) }
    }

    @Test
    fun failAnotherCreatingRoom_whenSurfaceExceedsTotalSurface(){
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        val roomSurface1 = totalSurface / 2
        val room1 = Room(randomUUID(), roomSurface1, flatId)
        roomStorage.storage.add(room1)

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.createRoom(totalSurface, flatId) }
    }

    @Test
    fun getRoom() {
        //GIVEN
        val roomId = randomUUID()
        val room = Room(roomId, Random.nextDouble() * 100, randomUUID())
        roomStorage.storage.add(room)

        //WHEN
        val foundRoom = flatConfigService.getRoom(roomId)

        //THEN
        assertThat(foundRoom).isEqualTo(room)
    }

    @Test
    fun failGettingRoom_whenNotExist() {
        //EXPECT
        assertThrows<RoomNotFoundException> { flatConfigService.getRoom(randomUUID()) }
    }

    @Test
    fun updateRoom(){
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomId = randomUUID()
        val room = Room(roomId, totalSurface/2, flatId)
        roomStorage.storage.add(room)

        val updatedSurface = totalSurface/3

        //WHEN
        val updatedRoom = flatConfigService.updateRoom(roomId, updatedSurface)

        //THEN
        assertThat(roomStorage.storage).hasSize(1)
        assertThat(roomStorage.storage.first()).isEqualTo(updatedRoom)
    }

    @Test
    fun failUpdatingRoom_whenNotExist(){
        //EXPECT
        assertThrows<RoomNotFoundException> { flatConfigService.updateRoom(randomUUID(), Random.nextDouble()) }
    }

    @Test
    fun failUpdatingRoom_whenSurfaceNegative(){
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomId = randomUUID()
        val room = Room(roomId, totalSurface/2, flatId)
        roomStorage.storage.add(room)

        val updatedSurface = totalSurface.unaryMinus()/3

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.updateRoom(roomId, updatedSurface) }
    }

    @Test
    fun failUpdatingRoom_whenSurfaceExceedsTotalSurface(){
        //SETUP
        val flatId = randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val flat = Flat(flatId, totalSurface)
        flatStorage.storage.add(flat)

        //GIVEN
        val roomId = randomUUID()
        val room = Room(roomId, totalSurface/2, flatId)
        roomStorage.storage.add(room)

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.updateRoom(roomId, totalSurface*2) }
    }

    @Test
    fun deleteRoom(){
        //SETUP
        val totalSurface = Random.nextDouble() * 100
        val flatId = randomUUID()
        flatStorage.storage.add(Flat(flatId, totalSurface))

        //GIVEN
        val roomId = randomUUID()
        roomStorage.storage.add(Room(roomId, totalSurface/2, flatId))

        //WHEN
        flatConfigService.deleteRoom(roomId)

        //THEN
        assertThat(roomStorage.storage).isEmpty()
    }

    @Test
    fun failDeletingRoom_whenNotExist(){
        //EXPECT
        assertThrows<RoomNotFoundException> { flatConfigService.deleteRoom(randomUUID()) }
    }
}