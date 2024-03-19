package org.example.migapi.repository

import org.example.migapi.domain.model.entity.Deadline
import org.springframework.data.jpa.repository.JpaRepository

interface DeadlineRepository : JpaRepository<Deadline, Deadline.DeadlineId> {
}