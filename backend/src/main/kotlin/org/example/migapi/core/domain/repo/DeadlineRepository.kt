package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Deadline
import org.springframework.data.jpa.repository.JpaRepository

interface DeadlineRepository : JpaRepository<Deadline, Deadline.DeadlineId> {
}