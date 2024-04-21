package org.example.migapi.core.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.enums.EStudentStatus

@Entity
@Table(name = "student_statuses")
data class StudentStatus(
    @Id
    @Enumerated(value = EnumType.STRING)
    val name: EStudentStatus = EStudentStatus.NONE
) : Model