package org.example.migapi.core.domain.service

import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.repo.RoleRepository
import org.example.migapi.core.domain.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val roleRepository: RoleRepository
) : UserService {

    override fun saveUser(userDto: UserDto): User {
        val role = findRoleByName(ERole.ROLE_USER.name)

        val user = User(
            username = userDto.username,
            password = userDto.password,
            role = role,
            isActive = userDto.isActive,
            tfaEnabled = userDto.tfaEnabled
        )

        return userRepository.save(user)
    }

    override fun saveUser(user: User): User = userRepository.save(user)

    override fun findUserByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { UserNotFoundException("User with username $username doesn't exists") }

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

    override fun dropTable() = userRepository.deleteAll()
}