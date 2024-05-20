package org.example.migapi.domain.typography.service

import org.example.migapi.*
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.domain.typography.dto.DocumentDto
import org.example.migapi.domain.typography.dto.TypographyDto
import org.example.migapi.domain.typography.dto.TypographyTitleDto
import org.example.migapi.domain.typography.model.DocumentType
import org.example.migapi.domain.typography.model.Typography
import org.example.migapi.domain.typography.repository.TypographyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class TypographyService(
    @Autowired
    private val typographyRepository: TypographyRepository,
    @Autowired
    private val dateFormatter: DateTimeFormatter
) {

    fun findAllTitlesByUsername(username: String, filterDate: String): List<TypographyTitleDto> {
        val date = filterDate.parseLocalDate(dateFormatter)

        return typographyRepository.findAllByUserUsername(getUsernameFromContext()).filter { date <= it.creationDate }
            .map { it.toTitleDto() }
    }

    fun findById(id: String): TypographyDto {
        val typography = typographyRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }
        val typographyDocuments = typography.documents
        val docTypes = typography.typographyType.documentList

        val documents = docTypes.map { type ->
            val doc = typographyDocuments
                .filter { it.documentType.name == type.name }
                .sortedByDescending { it.creationDate }.takeIf { it.isNotEmpty() }?.get(0)

            DocumentDto(
                id = doc?.id?.toString(),
                title = type.name,
                status = doc?.status?.toString(),
                fileName = doc?.fileName,
                creationDate = doc?.creationDate?.toDate(),
                expirationDate = doc?.expirationDate?.toDate()
            )
        }

        return TypographyDto(
            id = typography.id.toString(),
            title = typography.typographyType.name,
            status = typography.status.name,
            date = typography.creationDate.serialize(dateFormatter),
            documents = documents
        )
    }

    fun getTypographyListById(id: String): List<DocumentType> {
        val typography = typographyRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        return typography.typographyType.documentList
    }

    fun Typography.toTitleDto(): TypographyTitleDto = TypographyTitleDto(
        id = this.id.toString(),
        title = this.typographyType.name,
        status = this.status.name,
        date = this.creationDate.serialize(dateFormatter)
    )
}