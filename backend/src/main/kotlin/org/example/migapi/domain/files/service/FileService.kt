package org.example.migapi.domain.files.service

import jakarta.persistence.PersistenceException
import org.example.migapi.core.config.iof.config.FileSystemConfiguration
import org.example.migapi.domain.files.controller.FileController
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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import ru.homyakin.iuliia.Schemas
import ru.homyakin.iuliia.Translator
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.FileSystemException
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.util.*

@Service
class FileService(
    @Autowired
    private val fileSystemConfiguration: FileSystemConfiguration,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val fileRepository: FileRepository
) {

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            IOException::class,
            InvalidPathException::class,
            FileSystemException::class,
            PersistenceException::class
        ]
    )
    fun storeFile(file: MultipartFile, username: String): File {
        val user = userService.findUserByUsername(username)

        val translator = Translator(Schemas.GOST_52535)

        var fileName = file.originalFilename ?: throw FilenameNotFoundException()
        fileName = translator.translate(fileName)
        fileName = "${UUID.randomUUID()}_$fileName"

        Files.copy(file.inputStream, fileSystemConfiguration.path.resolve(fileName))

        val resultFile = File(
            name = fileName,
            user = user,
            link = MvcUriComponentsBuilder
                .fromMethodName(FileController::class.java, "uploadFile", fileName)
                .toString()
        )

        return resultFile.save()
    }

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            NoAccessException::class,
            FileNotFoundException::class,
            InvalidPathException::class,
            PersistenceException::class
        ]
    )
    fun load(fileName: String, username: String): Resource {
        val file = auth(fileName, username)

        val filePath = fileSystemConfiguration.path.resolve(file.name)

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

    @Throws(
        exceptionClasses = [
            UserNotFoundException::class,
            NoAccessException::class,
            FileNotFoundException::class,
            InvalidPathException::class,
            PersistenceException::class
        ]
    )
    fun delete(fileName: String, username: String): Boolean {
        val file = auth(fileName, username)

        val filePath = fileSystemConfiguration.path.resolve(file.name)


        return try {
            file.delete()
            Files.delete(filePath)
            true
        } catch (e: IOException) {
            throw FileNotFoundException()
        }
    }

    @Throws(exceptionClasses = [NoAccessException::class, PersistenceException::class])
    fun auth(fileName: String, username: String): File {
        val user = userService.findUserByUsername(username)
        val file = findByFileName(fileName)

        if (file.user.id != user.id)
            throw NoAccessException()

        return file
    }

    fun findByFileName(fileName: String): File =
        fileRepository.findById(fileName).orElseThrow { FileNotFoundException() }

    fun File.delete() = fileRepository.delete(this)

    fun File.save() = fileRepository.save(this)
}