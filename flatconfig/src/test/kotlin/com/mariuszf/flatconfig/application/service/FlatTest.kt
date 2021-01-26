package com.mariuszf.flatconfig.application.service

import com.mariuszf.flatconfig.application.exceptions.FlatNotFoundException
import com.mariuszf.flatconfig.application.exceptions.SurfaceIsInvalidException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.random.Random

class FlatTest : FlatConfigServiceTestBase() {

    @Test
    fun createFlat() {
        //GIVEN
        val totalSurface = Random.nextDouble() * 100

        //WHEN
        val flat = flatConfigService.createFlat(totalSurface)

        //THEN
        Assertions.assertThat(flat).isInstanceOf(Flat::class.java)
        Assertions.assertThat(flat.totalSurface).isEqualTo(totalSurface)
        Assertions.assertThat(flat.rooms).hasSize(0)
        Assertions.assertThat(flat.commonPartSurface).isEqualTo(totalSurface)
    }

    @Test
    fun failCreatingFlat_whenTotalSurfaceNegative() {
        //GIVEN
        val totalSurface = Random.nextDouble().unaryMinus() * 100

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.createFlat(totalSurface) }
    }

    @Test
    fun getFlat() {
        //GIVEN
        val flatId = UUID.randomUUID()
        val flat = Flat(flatId, Random.nextDouble() * 100)
        flatStorage.storage.add(flat)

        //WHEN
        val foundFlat = flatConfigService.getFlat(flatId)

        //THEN
        Assertions.assertThat(foundFlat).isEqualTo(flat)
    }

    @Test
    fun failFindingFlat_whenDoesNotExist() {
        //EXPECT
        assertThrows<FlatNotFoundException> { flatConfigService.getFlat(UUID.randomUUID()) }
    }

    @Test
    fun updateFlat() {
        //GIVEN
        val flatId = UUID.randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val updatedTotalSurface = totalSurface * 2
        flatStorage.storage.add(Flat(flatId, totalSurface))

        //WHEN
        val updatedFlat = flatConfigService.updateFlat(flatId, updatedTotalSurface)

        //THEN
        Assertions.assertThat(updatedFlat.id).isEqualTo(flatId)
        Assertions.assertThat(updatedFlat.totalSurface).isEqualTo(updatedTotalSurface)
        Assertions.assertThat(updatedFlat.rooms).hasSize(0)
        Assertions.assertThat(updatedFlat.commonPartSurface).isEqualTo(updatedTotalSurface)
    }

    @Test
    fun failUpdatingFlat_WhenFlatNotExist() {
        //GIVEN
        val flatId = UUID.randomUUID()
        val totalSurface = Random.nextDouble() * 100
        flatStorage.storage.removeIf { it.id == flatId }

        //EXPECT
        assertThrows<FlatNotFoundException> { flatConfigService.updateFlat(flatId, totalSurface) }
    }

    @Test
    fun failUpdatingFlat_WithNegativeSurface() {
        //GIVEN
        val flatId = UUID.randomUUID()
        val totalSurface = Random.nextDouble() * 100
        val updatedTotalSurface = totalSurface.unaryMinus()
        flatStorage.storage.add(Flat(flatId, totalSurface))

        //EXPECT
        assertThrows<SurfaceIsInvalidException> { flatConfigService.updateFlat(flatId, updatedTotalSurface) }
    }

    @Test
    fun deleteFlat() {
        //GIVEN
        val flatId = UUID.randomUUID()
        flatStorage.storage.add(Flat(flatId, Random.nextDouble() * 100))

        //WHEN
        flatConfigService.deleteFlat(flatId)

        //THEN
        Assertions.assertThat(flatStorage.storage.find { it.id == flatId }).isNull()
    }

    @Test
    fun failDeletingFlat_WhenDoesNotExist() {
        //EXPECT
        assertThrows<FlatNotFoundException> { flatConfigService.deleteFlat(UUID.randomUUID()) }
    }

}