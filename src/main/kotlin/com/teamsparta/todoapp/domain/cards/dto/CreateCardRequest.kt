package com.teamsparta.todoapp.domain.cards.dto

import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class CreateCardRequest(
    @field:Size(max = 500)
    val title: String,
    @field:Size(max = 5000)
    val description: String?,
    val imageUrl: MultipartFile?
)

