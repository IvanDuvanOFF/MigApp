package org.example.migapi.domain.typography.service

import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.domain.typography.dto.DocumentDto
import org.example.migapi.domain.typography.dto.TypographyDto
import org.example.migapi.domain.typography.dto.TypographyTitleDto
import org.example.migapi.domain.typography.event.ExpiredTypographyEvent
import org.example.migapi.domain.typography.model.Document
import org.example.migapi.domain.typography.model.DocumentStatus
import org.example.migapi.domain.typography.model.DocumentType
import org.example.migapi.domain.typography.model.Typography
import org.example.migapi.domain.typography.repository.TypographyAndRestRepository
import org.example.migapi.domain.typography.repository.TypographyRepository
import org.example.migapi.getUsernameFromContext
import org.example.migapi.toDate
import org.example.migapi.toUUID
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class TypographyService(
    @Autowired
    private val typographyRepository: TypographyRepository,
    @Autowired
    private val typographyAndRestRepository: TypographyAndRestRepository,
    @Autowired
    private val documentService: DocumentService,
    @Autowired
    private val migUtils: MigUtils,
    @Autowired
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun findAllTitlesByUsername(username: String, filterDate: String?): List<TypographyTitleDto> {
        val typographies = typographyRepository.findAllByUserUsername(getUsernameFromContext())

        return if (filterDate == null)
            typographies.map { it.toTitleDto() }
        else {
            val date = migUtils.stringToLocalDate(filterDate)

            typographies.filter { date <= it.creationDate }.map { it.toTitleDto() }
        }
    }

    @Transactional
    fun findById(id: String): TypographyDto {
        val typography = getById(id)
        val typographyDocuments = typography.documents
        val docTypes = typography.typographyType.documentList

        val documents = docTypes.map { type ->
            val doc = typographyDocuments?.let { doc ->
                doc.filter { it.documentType.name == type.name }.sortedByDescending { it.creationDate }
                    .takeIf { it.isNotEmpty() }?.get(0)
            }

            DocumentDto(
                id = doc?.id?.toString(),
                title = type.name,
                status = doc?.status?.toString()?.lowercase(),
                fileName = doc?.fileName,
                creationDate = doc?.creationDate?.toDate(),
                expirationDate = doc?.expirationDate?.toDate()
            )
        }

        return TypographyDto(
            id = typography.id.toString(),
            title = typography.typographyType.name,
            status = typography.status.name.lowercase(),
            date = migUtils.localDateToString(typography.creationDate),
            documents = documents
        )
    }

    fun addDocument(typographyId: String, documentDto: DocumentDto): DocumentDto {
        val typography = getById(typographyId)

        val doc = Document(
            id = UUID.randomUUID(),
            documentType = documentService.findTypeByName(documentDto.title),
            status = DocumentStatus.SAVED,
            typographyId = typography.id,
            creationDate = LocalDate.now(),
            fileName = documentDto.fileName
        )

        if (typography.documents == null)
            typography.documents = mutableSetOf(doc)
        else{
            typography.documents?.add(doc)
        }

        val result = documentService.saveDocument(doc)

        typographyRepository.save(typography)

        return result
    }

    fun getTypographyListById(id: String): List<DocumentType> {
        val typography = typographyRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        return typography.typographyType.documentList.toList()
    }

    fun Typography.toTitleDto(): TypographyTitleDto = TypographyTitleDto(
        id = this.id.toString(),
        title = this.typographyType.name,
        status = this.status.name,
        date = migUtils.localDateToString(this.creationDate)
    )

    fun getById(typographyId: String): Typography =
        typographyRepository.findById(typographyId.toUUID()).orElseThrow { NotFoundException() }

    @Scheduled(cron = "0 0 12 * * *")
    fun checkExpiration() {
        val typographies = typographyAndRestRepository.findAllByRestBetween()

        typographies.forEach { applicationEventPublisher.publishEvent(ExpiredTypographyEvent(this, it)) }
    }
}