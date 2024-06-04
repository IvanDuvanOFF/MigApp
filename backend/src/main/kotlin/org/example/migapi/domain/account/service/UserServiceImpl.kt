package org.example.migapi.domain.account.service

import com.google.api.gax.rpc.InvalidArgumentException
import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.domain.account.dto.UserDto
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.auth.model.Role
import org.example.migapi.domain.account.model.User
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.auth.repository.RoleRepository
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.domain.account.repository.UserRepository
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.domain.account.exception.CountryNotFoundException
import org.example.migapi.domain.account.exception.StatusNotFoundException
import org.example.migapi.domain.files.exception.NoAccessException
import org.example.migapi.utils.MigUtils
import org.jetbrains.annotations.TestOnly
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.format.DateTimeParseException
import java.util.*

/**
 * Сервис для работы с объектами пользователей [User]
 */
@Service
class UserServiceImpl(
    @Autowired
    private val dtoService: DtoService,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val roleRepository: RoleRepository,
    @Autowired
    private val migUtils: MigUtils
) : UserService {

    /**
     * Сохраняет пользователя [userDto]
     *
     * Только для тестов
     *
     * @throws CountryNotFoundException
     * @throws RoleNotFoundException
     * @throws DateTimeParseException
     * @throws StatusNotFoundException
     * @throws PersistenceException
     */
    @TestOnly
    override fun saveUser(userDto: UserDto): User {
        val user = dtoService.toUser(userDto)

        return userRepository.save(user)
    }

    /**
     * Сохраняет пользователя [user]
     *
     * @throws PersistenceException
     */
    override fun saveUser(user: User): User = userRepository.save(user)

    /**
     * Находит пользователей [List]<[User]> по роли [roleName]
     *
     * @throws PersistenceException
     */
    override fun findUsersByRole(roleName: ERole): List<User> = userRepository.findUsersByRole(Role(roleName))

    /**
     * Находит пользователя [User] по его id [id]
     *
     * @throws BadRequestException [id] имеет некорректный формат
     * @throws UserNotFoundException пользователь не найден
     */
    override fun findById(id: String): User = userRepository.findById(
        try {
            UUID.fromString(id)
        } catch (e: InvalidArgumentException) {
            throw BadRequestException("Invalid id")
        }
    ).orElseThrow { UserNotFoundException("User not found") }

    /**
     * Находит пользователя [User] по его username [username]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    override fun findUserByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { UserNotFoundException("User with username $username doesn't exists") }

    /**
     * Находит пользователя [User] по его username [username] и id [id]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    @Deprecated("Use findUserByUsername", replaceWith = ReplaceWith("findUsersByUsername(username)"))
    override fun findUserByUsernameAndId(username: String, id: String): User =
        findUserByUsername(username).takeIf { it.id.toString() == id } ?: throw NoAccessException()

    /**
     * Находит пользователя [User] по его email [email]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    override fun findUserByEmail(email: String): User = userRepository.findUserByEmail(email)
        .orElseThrow { UserNotFoundException("User with email $email doesn't exists") }

    /**
     * Находит пользователя [User] по его username или email [usernameOrEmail]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    override fun findUserByUsernameOrEmail(usernameOrEmail: String): User = when {
        migUtils.validateEmail(usernameOrEmail) -> findUserByEmail(usernameOrEmail)

        else -> findUserByUsername(usernameOrEmail)
    }

    /**
     * Находит пользователя [User] по его телефону или email [emailOrPhone]
     *
     * @throws UserNotFoundException пользователь не найден
     * @throws BadCredentialsException почта или телефон некорректного фармата
     */
    override fun findUserByEmailOrPhone(emailOrPhone: String): User = when {
        migUtils.validateEmail(emailOrPhone) -> findUserByEmail(emailOrPhone)
        migUtils.validatePhone(emailOrPhone) -> findUserByPhone(emailOrPhone)

        else -> throw BadCredentialsException("Email or phone are incorrect")
    }

    /**
     * Находит пользователя [User] по его по номеру телефона [phone]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    override fun findUserByPhone(phone: String): User = userRepository.findUserByPhone(phone)
        .orElseThrow { UserNotFoundException("User with phone $phone doesn't exists") }

    /**
     * Проверяет существует ли пользователь по его username [username]
     *
     * @return [Boolean]
     *
     * @throws UserNotFoundException пользователь не найден
     */
    override fun userExists(username: String): Boolean =
        userRepository.findUserByUsername(username).isPresent

    /**
     * Находит роль [Role] по ее имени [roleName]
     *
     * @throws RoleNotFoundException роль не найдена
     */
    override fun findRoleByName(roleName: String): Role = findRoleByERole(ERole.valueOf(roleName))

    /**
     * Находит роль [Role] по ее имени [roleEnum]
     *
     * @throws RoleNotFoundException роль не найдена
     */
    override fun findRoleByERole(roleEnum: ERole): Role = roleRepository.findById(roleEnum)
        .orElseThrow { RoleNotFoundException("No role $roleEnum found") }

    /**
     * Включает 2х факторную аутентификацию у пользователя по его id [id]
     *
     * @throws UserNotFoundException пользователь не найден
     * @throws PersistenceException
     */
    override fun enableTfa(id: UUID) {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User not found") }

        user.tfaEnabled = true

        userRepository.save(user)
    }

    /**
     * Удаляет пользователя по его id [id]
     *
     * @throws UserNotFoundException пользователь не найден
     * @throws PersistenceException
     */
    override fun deleteUserById(id: String) {
        val user = findById(id)

        userRepository.delete(user)
    }

    /**
     * Удаляет содержимое таблицы о пользователях
     *
     * Только для тестов
     *
     * @throws PersistenceException
     */
    @TestOnly
    override fun dropTable() = userRepository.deleteAll()
}