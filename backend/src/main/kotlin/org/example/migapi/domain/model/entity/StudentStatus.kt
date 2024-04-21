package org.example.migapi.domain.model.entity

import jakarta.persistence.*
import org.example.migapi.domain.model.Model
import org.example.migapi.domain.model.enums.EStudentStatus

@Entity
@Table(name = "student_statuses")
data class StudentStatus(
    @Id
    @Enumerated(value = EnumType.STRING)
    val name: EStudentStatus = EStudentStatus.NONE
) : Model