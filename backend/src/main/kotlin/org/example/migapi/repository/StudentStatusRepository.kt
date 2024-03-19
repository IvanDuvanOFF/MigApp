package org.example.migapi.repository

import org.example.migapi.domain.model.entity.StudentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface StudentStatusRepository : JpaRepository<StudentStatus, String> {
}