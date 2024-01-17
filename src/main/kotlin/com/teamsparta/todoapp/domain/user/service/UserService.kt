package com.teamsparta.todoapp.domain.user.service

import com.teamsparta.todoapp.domain.user.dto.SignUpRequest
import com.teamsparta.todoapp.domain.user.dto.UpdateUserProfileRequest
import com.teamsparta.todoapp.domain.user.dto.UserResponse


interface UserService {

    fun signUp(request: SignUpRequest): UserResponse

    fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse
}