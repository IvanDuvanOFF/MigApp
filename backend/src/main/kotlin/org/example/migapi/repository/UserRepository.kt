package org.example.migapi.repository

import org.example.migapi.domain.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findUserByUsername(username: String): Optional<User>

    fun findUserByEmail(email: String): Optional<User>
}