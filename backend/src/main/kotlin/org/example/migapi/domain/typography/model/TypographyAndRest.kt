package org.example.migapi.domain.typography.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.migapi.domain.account.model.User
import java.time.LocalDate
import java.util.UUID

/**
 * Сущность представления оформления и оставшихся дней до дедлайна
 *
 * @property id id оформления в системе [UUID]
 * @property typographyType тип оформления в системе [TypographyType]
 * @property user пользователь, к которому привязано это оформление в системе [User]
 * @property creationDate дата создания оформления в системе [LocalDate]
 * @property rest число дней до дедлайна
 */
@Entity
@Table(name = "typographies_and_rest")
data class TypographyAndRest(
    @Id
    val id: UUID,

    @ManyToOne(targetEntity = TypographyType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "typography_type")
    val typographyType: TypographyType,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "creation_date")
    val creationDate: LocalDate = LocalDate.now(),

    val rest: Int
)