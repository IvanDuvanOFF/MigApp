package org.example.migapi.domain.typography.service

import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.domain.typography.dto.DocumentDto
import org.example.migapi.domain.typography.model.Document
import org.example.migapi.domain.typography.model.DocumentStatus
import org.example.migapi.domain.typography.model.DocumentType
import org.example.migapi.domain.typography.repository.DocumentRepository
import org.example.migapi.domain.typography.repository.DocumentTypeRepository
import org.example.migapi.toDate
import org.example.migapi.toUUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class DocumentService(
    @Autowired
    private val documentRepository: DocumentRepository,
    @Autowired
    private val documentTypeRepository: DocumentTypeRepository
) {

    fun saveDocument(typographyId: String, documentDto: DocumentDto): DocumentDto {
        val doc = Document(
            id = UUID.randomUUID(),
            documentType = findTypeByName(documentDto.title),
            status = DocumentStatus.SAVED,
            typographyId = typographyId.toUUID(),
            creationDate = LocalDate.now(),
            fileName = documentDto.fileName
        )

        return documentRepository.save(doc).toDto()
    }

    fun getById(id: String): DocumentDto =
        documentRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }.toDto()

    private fun findTypeByName(name: String): DocumentType =
        documentTypeRepository.findById(name).orElseThrow { NotFoundException() }

    private fun Document.toDto() = DocumentDto(
        id = this.id.toString(),
        title = this.documentType.name,
        status = this.status.name,
        creationDate = this.creationDate?.toDate(),
        expirationDate = this.expirationDate?.toDate(),
        fileName = this.fileName
    )
}