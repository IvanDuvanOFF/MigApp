package org.example.migapi.repository

import org.example.migapi.domain.model.entity.TfaCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TfaCodeRepository : JpaRepository<TfaCode, String> {
    fun findTfaCodeByTfaId(tfaCodeId: TfaCode.TfaCodeId): Optional<TfaCode>
}