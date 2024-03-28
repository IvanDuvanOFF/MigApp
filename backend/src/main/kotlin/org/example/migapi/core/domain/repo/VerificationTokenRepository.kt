package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VerificationTokenRepository : JpaRepository<VerificationToken, UUID> {
}