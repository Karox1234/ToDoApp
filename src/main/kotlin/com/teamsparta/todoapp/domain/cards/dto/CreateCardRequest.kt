package com.teamsparta.todoapp.domain.cards.dto

import java.time.OffsetDateTime


data class CreateCardRequest(
    val title: String,
    val description: String?,
    val writer: String?,
    val createdAt: OffsetDateTime,
    val completed: Boolean = false
)

