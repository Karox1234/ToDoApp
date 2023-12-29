package com.teamsparta.todoapp.domain.cards.dto

import java.time.OffsetDateTime

data class UpdateCardRequest(
    val title: String,
    val description: String?,
    val writer : String?,
    val createdAT : OffsetDateTime
)