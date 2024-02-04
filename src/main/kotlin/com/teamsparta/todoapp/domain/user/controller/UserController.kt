package com.teamsparta.todoapp.domain.user.controller


import com.teamsparta.todoapp.domain.user.dto.*
import com.teamsparta.todoapp.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder


@Validated
@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/login")
    fun signIn(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(loginRequest))

    }

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequest))
    }

    @PutMapping("/users/{userId}/profile")
    fun updateUserProfile(@Valid @PathVariable userId: Long,
                          @RequestBody updateUserProfileRequest: UpdateUserProfileRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateUserProfile(userId, updateUserProfileRequest))
    }

    @GetMapping("/checkNickname")
    fun checkNickname(@Valid @RequestParam nickname: String):
            ResponseEntity<String> {
        userService.beforeSignUpCheckNickname(BeforeSignUpCheckNicknameRequest(nickname))
        return ResponseEntity.ok("사용 가능한 닉네임 입니다")
    }

    @PostMapping("/mail")
    fun sendMail(@RequestParam email: String):ResponseEntity<String> {
        userService.sendMail(SendMail(email))
        return ResponseEntity.ok("인증 메일을 발송 하였습니다")
    }
}