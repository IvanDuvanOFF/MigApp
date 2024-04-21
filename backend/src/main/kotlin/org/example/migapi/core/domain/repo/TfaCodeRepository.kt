package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.TfaCode
import org.example.migapi.core.domain.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TfaCodeRepository : JpaRepository<TfaCode, TfaCode.TfaCodeId> {
    fun findTfaCodesByTfaIdUser(user: User): List<TfaCode>
}