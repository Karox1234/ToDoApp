package com.teamsparta.todoapp.domain.user.controller


import com.teamsparta.todoapp.domain.user.dto.*
import com.teamsparta.todoapp.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


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
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequest))
    }

    @PutMapping("/users/{userId}/profile")
    fun updateUserProfile(@PathVariable userId: Long,
                          @RequestBody updateUserProfileRequest: UpdateUserProfileRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateUserProfile(userId, updateUserProfileRequest))
    }
}