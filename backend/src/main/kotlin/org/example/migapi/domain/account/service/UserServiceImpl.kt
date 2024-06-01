package org.example.migapi.domain.account.service

import com.google.api.gax.rpc.InvalidArgumentException
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
import org.example.migapi.domain.files.exception.NoAccessException
import org.example.migapi.utils.MigUtils
import org.jetbrains.annotations.TestOnly
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

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

    @TestOnly
    override fun saveUser(userDto: UserDto): User {
        val user = dtoService.toUser(userDto)

        return userRepository.save(user)
    }

    override fun saveUser(user: User): User = userRepository.save(user)

    override fun findUsersByRole(roleName: ERole): List<User> = userRepository.findUsersByRole(Role(roleName))

    override fun findById(id: String): User = userRepository.findById(
        try {
            UUID.fromString(id)
        } catch (e: InvalidArgumentException) {
            throw BadRequestException("Invalid id")
        }
    ).orElseThrow { UserNotFoundException("User not found") }

    override fun findUserByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { UserNotFoundException("User with username $username doesn't exists") }

    override fun findUserByUsernameAndId(username: String, id: String): User =
        findUserByUsername(username).takeIf { it.id.toString() == id } ?: throw NoAccessException()

    override fun findUserByEmail(email: String): User = userRepository.findUserByEmail(email)
        .orElseThrow { UserNotFoundException("User with email $email doesn't exists") }

    override fun findUserByUsernameOrEmail(usernameOrEmail: String): User = when {
        migUtils.validateEmail(usernameOrEmail) -> findUserByEmail(usernameOrEmail)

        else -> findUserByUsername(usernameOrEmail)
    }

    override fun findUserByEmailOrPhone(emailOrPhone: String): User = when {
        migUtils.validateEmail(emailOrPhone) -> findUserByEmail(emailOrPhone)
        migUtils.validatePhone(emailOrPhone) -> findUserByPhone(emailOrPhone)

        else -> throw BadCredentialsException("Email or phone are incorrect")
    }

    override fun findUserByPhone(phone: String): User = userRepository.findUserByPhone(phone)
        .orElseThrow { UserNotFoundException("User with phone $phone doesn't exists") }

    override fun userExists(username: String): Boolean =
        userRepository.findUserByUsername(username).isPresent

    override fun findRoleByName(roleName: String): Role = findRoleByERole(ERole.valueOf(roleName))

    override fun findRoleByERole(roleEnum: ERole): Role = roleRepository.findById(roleEnum)
        .orElseThrow { RoleNotFoundException("No role $roleEnum found") }


    override fun enableTfa(id: UUID) {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User not found") }

        user.tfaEnabled = true

        userRepository.save(user)
    }

    override fun deleteUserById(id: String) {
        val user = findById(id)

        userRepository.delete(user)
    }

    @TestOnly
    override fun dropTable() = userRepository.deleteAll()
}