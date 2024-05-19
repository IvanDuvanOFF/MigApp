package org.example.migapi.domain.typography.repository

import org.example.migapi.domain.typography.model.Deadline
import org.springframework.data.jpa.repository.JpaRepository

interface DeadlineRepository : JpaRepository<Deadline, Deadline.DeadlineId> {
}