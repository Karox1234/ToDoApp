package com.teamsparta.todoapp.domain.user.service



import com.teamsparta.todoapp.domain.exception.InvalidCredentialException
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.user.dto.*
import com.teamsparta.todoapp.domain.user.model.Profile
import com.teamsparta.todoapp.domain.user.model.User
import com.teamsparta.todoapp.domain.user.model.UserRole
import com.teamsparta.todoapp.domain.user.model.toResponse
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import com.teamsparta.todoapp.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service



@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
): UserService {

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null)

        if (user.role.name != request.role || passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialException()

        }
        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }


    @Transactional
    override fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }

        return userRepository.save(
            User(
                email = request.email,
                //패스워드 암호화
                password = passwordEncoder.encode(request.password),
                profile = Profile(
                    nickname = request.nickname
                ),
                role = when (request.role) {
                    UserRole.ADMIN.name -> UserRole.ADMIN
                    UserRole.USER.name -> UserRole.USER
                    else -> throw IllegalArgumentException("Invalid role")
                }
            )
        ).toResponse()
    }

    @Transactional
    override fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        user.profile = Profile(
            nickname = request.nickname
        )

        return userRepository.save(user).toResponse()
    }

}