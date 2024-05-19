package org.example.migapi.domain.typography.service

import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.domain.typography.dto.DocumentTitleDto
import org.example.migapi.domain.typography.dto.TypographyDto
import org.example.migapi.domain.typography.dto.TypographyTitleDto
import org.example.migapi.domain.typography.model.Typography
import org.example.migapi.domain.typography.repository.TypographyRepository
import org.example.migapi.getUsernameFromContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TypographyService(
    @Autowired
    private val typographyRepository: TypographyRepository
) {

    fun findAllTitlesByUsername(username: String): List<TypographyTitleDto> =
        typographyRepository.findAllByUserUsername(getUsernameFromContext()).map { it.toTitleDto() }

    fun findById(id: String): TypographyDto {
        val typography = typographyRepository.findById(
            try {
                UUID.fromString(id)
            } catch (e: IllegalArgumentException) {
                throw BadRequestException()
            }
        ).orElseThrow { NotFoundException() }

        return typography.toDto()
    }

    fun Typography.toTitleDto(): TypographyTitleDto = TypographyTitleDto(
        id = this.id.toString(),
        title = this.typographyType.name,
        status = this.status.name
    )

    fun Typography.toDto(): TypographyDto = TypographyDto(
        id = this.id.toString(),
        title = this.typographyType.name,
        status = this.status.name,
        documents = this.documents.map {
            DocumentTitleDto(
                id = it.id.toString(),
                title = it.documentType.name,
                status = it.status,
                link = it.link ?: ""
            )
        }
    )
}