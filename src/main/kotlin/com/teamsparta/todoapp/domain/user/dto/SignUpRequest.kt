package com.teamsparta.todoapp.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpRequest(
    val email: String,
    @Schema(description = "password는 4자이상 대소문자와 숫자로만 이루어져야 합니다", example = "1s2N")
    @field:Size(min = 4)
    @field:Pattern(regexp = "^[a-zA-Z0-9]*\$",
        message = "password는 닉네임과 같은 값이 포함되면 안됩니다, 4자이상의 비밀번호를 입력해주세요")
    val password: String,
    @Schema(description = "nickname은 3자이상 대소문자와 숫자로만 이루어져야 합니다", example = "9Ab")
    @field:Size(min = 3)
    @field:Pattern(regexp = "^[a-zA-Z0-9]*\$",
        message = "3자 이상의 nickname을 입력해 주세요, nickname과 password는 같은 값이 포함되어선 안됩니다.")
    val nickname: String,
    val verificationCode: Int,
    @Schema(description = "역할", example = "USER")
    val role: String,
)

