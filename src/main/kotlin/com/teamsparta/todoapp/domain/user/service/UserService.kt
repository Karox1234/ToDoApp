package com.teamsparta.todoapp.domain.user.service

import com.teamsparta.todoapp.domain.user.dto.*


interface UserService {

    fun signUp(request: SignUpRequest): UserResponse

    fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse

    fun login(request: LoginRequest): LoginResponse
}