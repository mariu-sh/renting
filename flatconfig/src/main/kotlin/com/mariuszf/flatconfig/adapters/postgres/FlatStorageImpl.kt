package com.mariuszf.flatconfig.adapters.postgres

import com.mariuszf.flatconfig.application.port.out.FlatStorage
import com.mariuszf.flatconfig.application.service.Flat
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
interface FlatStorageImpl: FlatStorage, CrudRepository<FlatEntity, UUID> {

    override fun findFlatById(flatId: UUID): Flat {
        val flatEntity = findById(flatId)
        return Optional.of(flatEntity)
                .map { it.get().toDomain() }
                .orElseThrow{ Exception() }
    }

    override fun createFlat(totalSurface: Double): Flat {
        val flatEntity = FlatEntity()
        flatEntity.totalSurface = totalSurface
        return save(flatEntity).toDomain()
    }

    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat {
        val flatEntity = findById(flatId).get()
        flatEntity.totalSurface = totalSurface
        return save(flatEntity).toDomain()
    }

    override fun deleteFlatById(flatId: UUID) = deleteById(flatId)
}

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "flat")
class FlatEntity{

    @Id
    @GeneratedValue
    @Column(name = "flat_id")
    lateinit var flatId: UUID

    @Column(name = "total_surface", nullable = false)
    var totalSurface: Double = 0.0

    fun toDomain(): Flat = Flat(flatId, totalSurface)
}