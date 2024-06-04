package org.example.migapi.domain.typography.service

import jakarta.persistence.PersistenceException
import org.example.migapi.core.config.exception.*
import org.example.migapi.domain.typography.dto.DocumentDto
import org.example.migapi.domain.typography.model.Document
import org.example.migapi.domain.typography.model.DocumentType
import org.example.migapi.domain.typography.repository.DocumentRepository
import org.example.migapi.domain.typography.repository.DocumentTypeRepository
import org.example.migapi.toUUID
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Сервис для работы с сущностями документов [Document]
 */
@Service
class DocumentService(
    @Autowired
    private val documentRepository: DocumentRepository,
    @Autowired
    private val documentTypeRepository: DocumentTypeRepository,
    @Autowired
    private val migUtils: MigUtils
) {

    /**
     * Метод сохраняет документ в систему
     *
     * @return [DocumentDto]
     *
     * @throws InternalServerException
     * @throws PersistenceException
     */
    fun saveDocument(document: Document): DocumentDto = documentRepository.save(document).toDto()

    fun getById(id: String): DocumentDto =
        documentRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }.toDto()

    /**
     * Метод находит тип документа [DocumentType] по его имени [name]
     *
     * @throws NotFoundException
     */
    fun findTypeByName(name: String): DocumentType =
        documentTypeRepository.findById(name).orElseThrow { NotFoundException() }

    /**
     * Переводит [Document] в [DocumentDto] для последующей передачи на клиент
     *
     * @throws InternalServerException
     */
    private fun Document.toDto() = DocumentDto(
        id = this.id.toString(),
        title = this.documentType.name,
        status = this.status.name,
        creationDate = this.creationDate?.let { migUtils.localDateToString(it) },
        expirationDate = this.expirationDate?.let { migUtils.localDateToString(it) },
        fileName = this.fileName
    )
}