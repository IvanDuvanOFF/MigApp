package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.enums.ERole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, ERole> {
}