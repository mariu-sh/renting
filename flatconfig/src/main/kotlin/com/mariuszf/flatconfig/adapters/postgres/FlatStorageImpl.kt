package com.mariuszf.flatconfig.adapters.postgres

import com.mariuszf.flatconfig.application.port.out.FlatStorage
import com.mariuszf.flatconfig.application.service.Flat
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Component
class FlatStorageImpl(val postgresFlatStorage: PostgresFlatStorage) : FlatStorage {

    @Transactional
    override fun createFlat(totalSurface: Double): Flat =
        postgresFlatStorage.save(FlatEntity(UUID.randomUUID(), totalSurface)).toDomain()

    override fun updateFlat(flatId: UUID, totalSurface: Double): Flat {
        val flatEntity = postgresFlatStorage.findById(flatId).get()
        flatEntity.totalSurface = totalSurface
        return postgresFlatStorage.save(flatEntity).toDomain()
    }

    override fun findFlatById(flatId: UUID): Flat =
        postgresFlatStorage.findById(flatId).map { it.toDomain() }
            .orElseThrow { FlatNotFoundInStorageException("Flat with id $flatId not found") }

    override fun findAllFlats(): List<Flat> = postgresFlatStorage.findAll().map { it.toDomain() }

    override fun deleteFlatById(flatId: UUID) =
        try {
            postgresFlatStorage.deleteById(flatId)
        } catch (_: EmptyResultDataAccessException) {
            throw FlatNotFoundInStorageException("Flat with id $flatId not found")
        }
}

@Repository
interface PostgresFlatStorage : JpaRepository<FlatEntity, UUID>

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "flat")
data class FlatEntity(
    @Id @Column(name = "flat_id") val id: UUID?,
    @Column(name = "total_surface") var totalSurface: Double
) {
    constructor() : this(null, 0.0)

    fun toDomain(): Flat = Flat(id!!, totalSurface)
}