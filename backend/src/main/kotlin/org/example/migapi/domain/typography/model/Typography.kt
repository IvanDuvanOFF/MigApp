package org.example.migapi.domain.typography.model

import jakarta.persistence.*
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User
import java.time.LocalDate
import java.util.*

/**
 * Сущность заявки на оформления.
 *
 * @property id id оформления в системе [UUID]
 * @property typographyType тип оформления [TypographyType]
 * @property user пользователь, к которому относится данное оформление
 * @property creationDate дата создания оформления в системе [LocalDate]
 * @property fileName имя файла готового оформления в системе [String]
 * @property status статус оформления в системе [TypographyStatus]
 * @property documents множество документов, приложенных к данному оформлению [MutableSet]<[Document]>
 */
@Entity
@Table(name = "typographies")
data class Typography(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(targetEntity = TypographyType::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "typography_type")
    val typographyType: TypographyType,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "creation_date")
    val creationDate: LocalDate = LocalDate.now(),

    @Column(name = "file_name")
    var fileName: String? = null,

    @ManyToOne(targetEntity = TypographyStatus::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    var status: TypographyStatus,

    @OneToMany(targetEntity = Document::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "typography_id")
    var documents: MutableSet<Document>?
) : Model