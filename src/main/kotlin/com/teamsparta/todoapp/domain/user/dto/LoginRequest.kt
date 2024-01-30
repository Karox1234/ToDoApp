package com.teamsparta.todoapp.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class LoginRequest(
    val email: String,
    val password: String,
    @Schema(description = "역할", example = "USER")
    val role: String
)
