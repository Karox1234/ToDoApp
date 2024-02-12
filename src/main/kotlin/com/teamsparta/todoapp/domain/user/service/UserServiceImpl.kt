package com.teamsparta.todoapp.domain.user.service


import com.teamsparta.todoapp.domain.exception.InvalidCredentialException
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.user.dto.*
import com.teamsparta.todoapp.domain.user.model.*
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import com.teamsparta.todoapp.domain.user.repository.VerificationRepository
import com.teamsparta.todoapp.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random


@Transactional
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val javaMailSender: JavaMailSender,
    private val verificationRepository: VerificationRepository
) : UserService {

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null)
        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)) {
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
        if (userRepository.existsByProfileNickname(request.nickname)) {
            throw IllegalStateException("이미 사용중인 Nickname 입니다.")
        }
    }

    override fun sendMail(request: SendMail) {
        val verificationCode = Random.nextInt(100000, 999999)
        val verificationInfo = Verification(
            email = request.email,
            verificationCode = verificationCode,
            expirationTime = System.currentTimeMillis() + 5 * 60 * 1000 // 5분
        )
        verificationRepository.save(verificationInfo)


        val subject = "${request.email}님 환영합니다, 인증 번호 확인 메일입니다"
        val content = "인증 번호: $verificationCode"

        val message = javaMailSender.createMimeMessage()
        val messageDetail = MimeMessageHelper(message)

        messageDetail.setTo(request.email)
        messageDetail.setSubject(subject)
        messageDetail.setText(content)

        javaMailSender.send(message)
    }

    override fun signUp(request: SignUpRequest): UserResponse {


        val verificationInfo =
            verificationRepository.findByEmailAndVerificationCode(request.email, request.verificationCode)
                ?: throw IllegalArgumentException("잘못된 인증번호 입니다.")


        if (System.currentTimeMillis() > verificationInfo.expirationTime) {
            throw IllegalArgumentException("인증번호가 만료 되었습니다.")
        }

        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }

        if (userRepository.existsByProfileNickname(request.nickname)) {
            throw IllegalStateException("이미 사용중인 Nickname 입니다.")
        }

        if (request.password.windowed(4, 1).any { it in request.nickname } ||
            request.nickname.windowed(4, 1).any { it in request.password })
            throw IllegalArgumentException("닉네임과 비밀번호는 서로의 값은 포함해선 안됩니다.")

        val saveUser = userRepository.save(
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
        )
//        verificationRepository.delete(verificationInfo) 스케쥴러 구현후 생각해보니 굳이 지금 지울 필요가 없다 판단!
        return saveUser.toResponse()
    }



    override fun updateUserProfile(userId: Long, updateUserProfileRequest: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val newNickname = updateUserProfileRequest.nickname

        if (newNickname != user.profile.nickname) {
            user.profile = Profile(
                nickname = newNickname
            )
        }

        return userRepository.save(user).toResponse()
    }

}
