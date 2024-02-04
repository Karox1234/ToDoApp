package com.teamsparta.todoapp.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class BeforeSignUpCheckNicknameRequest(
    @Schema(description = "nickname은 3자이상 대소문자와 숫자로만 이루어져야 합니다", example = "9Ab")
    @field:Size(min = 3)
    @field:Pattern(regexp = "^[a-zA-Z0-9]*\$",
        message = "3자 이상의 nickname을 입력해 주세요, nickname과 password는 같은 값이 포함되어선 안됩니다.")
    val nickname: String,
)


