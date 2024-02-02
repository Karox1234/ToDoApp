package com.teamsparta.todoapp.domain.user.service



import com.teamsparta.todoapp.domain.exception.InvalidCredentialException
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.user.dto.*
import com.teamsparta.todoapp.domain.user.model.*
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import com.teamsparta.todoapp.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) : UserService {

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null )
        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password) ) {
            throw InvalidCredentialException("비밀번호가 틀립니다")
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }

    override fun beforeSignUpCheckNickname(request: BeforeSignUpCheckNicknameRequest) {
        if(userRepository.existsByProfileNickname(request.nickname)){
            throw IllegalStateException("이미 사용중인 Nickname 입니다.")
        } //같은 닉네임 사용 불가 추가
    }

    override fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }
        return userRepository.save(
            UserEntity(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                profile = Profile(
                    nickname = request.nickname
                ),
                role = when (request.role) {
                    "ADMIN" -> UserRole.ADMIN
                    "USER" -> UserRole.USER
                    else -> throw IllegalArgumentException("Invalid role")
                }
            )
        ).toResponse()
    }

    override fun updateUserProfile(userId: Long, updateUserProfileRequest: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        user.profile = Profile(
            nickname = updateUserProfileRequest.nickname
        )

        return userRepository.save(user).toResponse()
    }

}