package com.teamsparta.todoapp.domain.user.service



import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.user.dto.SignUpRequest
import com.teamsparta.todoapp.domain.user.dto.UpdateUserProfileRequest
import com.teamsparta.todoapp.domain.user.dto.UserResponse
import com.teamsparta.todoapp.domain.user.model.Profile
import com.teamsparta.todoapp.domain.user.model.User
import com.teamsparta.todoapp.domain.user.model.UserRole
import com.teamsparta.todoapp.domain.user.model.toResponse
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service



@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {

    @Transactional
    override fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }

        return userRepository.save(
            User(
                email = request.email,
                // TODO: password 암호화 필요(인증/인가 단계에서)
                password = request.password,
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