package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.StudentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface StudentStatusRepository : JpaRepository<StudentStatus, String> {
}