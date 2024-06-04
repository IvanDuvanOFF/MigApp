package org.example.migapi.domain.files.service

import jakarta.persistence.PersistenceException
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.config.iof.config.FileSystemConfiguration
import org.example.migapi.domain.files.exception.FileNotFoundException
import org.example.migapi.domain.files.exception.FilenameNotFoundException
import org.example.migapi.domain.files.exception.NoAccessException
import org.example.migapi.domain.files.model.File
import org.example.migapi.domain.files.repository.FileRepository
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.domain.account.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.homyakin.iuliia.Schemas
import ru.homyakin.iuliia.Translator
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.*
import java.util.*

/**
 * Сервис для работы с файлами [File]
 */
@Service
class FileService(
    @Autowired
    private val fileSystemConfiguration: FileSystemConfiguration,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val fileRepository: FileRepository
) {

    /**
     * Сохраняет файл [file] в файловой системе и добавляет информациою о нем в бд,
     * прикрепляя его к пользователю по его [username]. А так же транслитирует название файла на латиницу.
     *
     * @throws FilenameNotFoundException если имя файла не найдено
     * @throws BadRequestException если имя файла содержит некорректные символы
     * @throws FileSystemException если путь до папки некорректный
     * @throws UserNotFoundException если пользователь не найден
     * @throws IOException если файл не получилось сохранить
     * @throws InvalidPathException если путь до папки некорректный
     * @throws PersistenceException
     */
    fun storePrivateFile(file: MultipartFile, username: String): File {
        val user = userService.findUserByUsername(username)

        val translator = Translator(Schemas.GOST_52535)
        val regex = "^[a-zA-Z0-9]*\$"

        var fileName = file.originalFilename ?: throw FilenameNotFoundException()
        fileName = translator.translate(fileName)
        fileName = "${UUID.randomUUID()}_$fileName"

        if (fileName.matches(regex.toRegex()))
            throw BadRequestException("File name contains invalid characters")

        val path = fileSystemConfiguration.path.resolve("${user.id}")
        if (!Files.exists(path))
            Files.createDirectory(path)

        Files.copy(file.inputStream, fileSystemConfiguration.path.resolve("${user.id}/$fileName"))

        val resultFile = File(
            name = fileName,
            user = user,
            link = "/api/files/$fileName"
        )

        return resultFile.save()
    }

    /**
     * Загружает файл [Resource] из приватной папки пользователя [username] по его имени [fileName]
     *
     * @throws FileSystemException если путь до папки некорректный
     * @throws UserNotFoundException если пользователь не найден
     * @throws NoAccessException если у пользователя нет доступа к файлу
     * @throws FileNotFoundException если файл не найден
     * @throws IOException если файл не получилось сохранить
     * @throws InvalidPathException если путь до папки некорректный
     * @throws PersistenceException
     */
    fun loadPrivateFile(fileName: String, username: String): Resource {
        val file = auth(fileName, username)

        val filePath = fileSystemConfiguration.path.resolve("${file.user.id}/${file.name}")

        return loadResource(filePath)
    }

    /**
     * Загружает публичный файл [Resource] из общего хранилища по его [fileName]
     *
     * @throws FileNotFoundException если файл не найден
     * @throws InvalidPathException если путь до папки некорректный
     */
    @Throws(
        exceptionClasses = [
            FileNotFoundException::class,
            InvalidPathException::class,
        ]
    )
    fun loadPublicFile(fileName: String): Resource {
        val filePath = fileSystemConfiguration.publicPath.resolve(fileName)

        return loadResource(filePath)
    }

    /**
     * Удаляет файл из приватной папки пользователя [username] по его имени [fileName]
     *
     * @return true, если удаление файла прошло успешно, false - если нет
     *
     * @throws UserNotFoundException если пользователь не найден
     * @throws NoAccessException если у пользователя нет доступа к файлу
     * @throws FileNotFoundException если файл не найден
     * @throws InvalidPathException если путь до папки некорректный
     * @throws PersistenceException
     */
    fun deletePrivateFile(fileName: String, username: String): Boolean {
        val file = auth(fileName, username)

        val filePath = fileSystemConfiguration.path.resolve("${file.user.id}/${file.name}")

        return try {
            file.delete()
            Files.delete(filePath)
            true
        } catch (e: IOException) {
            throw FileNotFoundException()
        }
    }

    /**
     * Загружает файл [File] из приватной папки пользователя [username] по его имени [fileName]
     *
     * @throws NoAccessException если у пользователя нет доступа к файлу
     * @throws UserNotFoundException если пользователь не найден
     * @throws FileNotFoundException если файл не найден
     * @throws PersistenceException
     */
    @Throws(exceptionClasses = [NoAccessException::class, PersistenceException::class])
    fun auth(fileName: String, username: String): File {
        val user = userService.findUserByUsername(username)
        val file = findByFileName(fileName)

        if (file.user.id != user.id)
            throw NoAccessException()

        return file
    }

    /**
     * Находит файл [File] по его имени [fileName]
     *
     * @throws FileNotFoundException если файл не найден
     * @throws PersistenceException
     */
    fun findByFileName(fileName: String): File =
        fileRepository.findById(fileName).orElseThrow { FileNotFoundException() }

    /**
     * Удаляет сведения о файле [File] из бд
     *
     * @throws PersistenceException
     */
    fun File.delete() = fileRepository.delete(this)

    /**
     * Сохраняет сведения о файле [File] в бд
     *
     * @throws PersistenceException
     */
    fun File.save() = fileRepository.save(this)

    /**
     * Загружает ресурс [Resource] по его пути [filePath]
     *
     * @throws FileNotFoundException если путь до файла невалиден
     */
    private fun loadResource(filePath: Path): Resource {
        val resource = try {
            UrlResource(filePath.toUri())
        } catch (e: MalformedURLException) {
            throw FileNotFoundException()
        }

        return if (resource.exists() && resource.isReadable)
            resource
        else
            throw FileNotFoundException()
    }
}