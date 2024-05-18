package org.example.migapi.domain.account.model

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