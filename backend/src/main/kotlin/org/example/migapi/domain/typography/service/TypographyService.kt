package org.example.migapi.domain.typography.service

import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.core.config.exception.*
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
import org.example.migapi.toUUID
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

/**
 * Сервис для работы с оформлениями пользователей
 */
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

    /**
     * Метод для получения всех оформлений студента [username] в зависимости от даты создания [filterDate]
     *
     * @return [List]<[TypographyTitleDto]>
     *
     * @throws PersistenceException
     */
    fun findAllTitlesByUsername(username: String, filterDate: String?): List<TypographyTitleDto> {
        val typographies = typographyRepository.findAllByUserUsername(getUsernameFromContext())

        return if (filterDate == null)
            typographies.map { it.toTitleDto() }
        else {
            val date = migUtils.stringToLocalDate(filterDate)

            typographies.filter { date <= it.creationDate }.map { it.toTitleDto() }
        }
    }

    /**
     * Метод получения оформления по его [id]. Массив [TypographyDto.documents] заполняется
     * типами документов, если документ не прикреплен, и [DocumentDto] если документ прикреплен
     *
     * @return [TypographyDto]
     *
     * @throws NotFoundException если оформление не найдено
     * @throws InternalServerException если произошла ошибка во время конвертации даты и времени
     * @throws PersistenceException
     */
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
                creationDate = doc?.creationDate?.let { migUtils.localDateToString(it) },
                expirationDate = doc?.expirationDate?.let { migUtils.localDateToString(it) }
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

    /**
     * Метод добавления нового документа [documentDto] в оформление по его id [typographyId]
     *
     * @return [DocumentDto]
     *
     * @throws NotFoundException если оформление или тип документа не найдены
     * @throws PersistenceException
     */
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

    /**
     * Метод получения списка типов документов [List]<[DocumentType]>, необходимых для оформления с id [id]
     *
     * @throws NotFoundException если оформление не найдено
     * @throws PersistenceException
     */
    fun getTypographyListById(id: String): List<DocumentType> {
        val typography = typographyRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        return typography.typographyType.documentList.toList()
    }

    /**
     * Метод для сохранения оформления [typography]
     *
     * @throws PersistenceException
     */
    fun save(typography: Typography) {
        typographyRepository.save(typography)
    }

    fun Typography.toTitleDto(): TypographyTitleDto = TypographyTitleDto(
        id = this.id.toString(),
        title = this.typographyType.name,
        status = this.status.name,
        date = migUtils.localDateToString(this.creationDate)
    )

    /**
     * Метод получения оформления [Typography] по его id [typographyId]
     *
     * @throws NotFoundException если оформление не найдено
     * @throws PersistenceException
     */
    fun getById(typographyId: String): Typography =
        typographyRepository.findById(typographyId.toUUID()).orElseThrow { NotFoundException() }

    /**
     * Шедулер для провеврки оформления на истечение дедлайна.
     * Получает список просрочившихся и близких к этому оформлений и создает событие
     * для каждого такого оформления.
     */
    @Scheduled(cron = "0 0 12 * * *")
    fun checkExpiration() {
        val typographies = typographyAndRestRepository.findAllByRestBetween()

        typographies.forEach { applicationEventPublisher.publishEvent(ExpiredTypographyEvent(this, it)) }
    }
}