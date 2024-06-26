package org.example.migapi.domain.files.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.migapi.core.domain.model.Model
import org.example.migapi.domain.account.model.User

/**
 * Сущность файла в системе.
 *
 * @property name имя файла в системе. Задается в формате <uuid>_<original_file_name>.
 * @property link ссылка на файл
 * @property user пользователь, которому принадлежит файл
 */
@Entity
@Table(name = "files")
data class File(
    @Id
    val name: String,

    val link: String,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    val user: User
) : Model
