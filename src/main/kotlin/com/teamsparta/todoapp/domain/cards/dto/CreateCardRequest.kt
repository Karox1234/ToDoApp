package com.teamsparta.todoapp.domain.cards.dto




data class CreateCardRequest(
    val title: String, val description: String?, val writer: String?
)

