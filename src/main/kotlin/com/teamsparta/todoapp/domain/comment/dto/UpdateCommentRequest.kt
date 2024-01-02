package com.teamsparta.todoapp.domain.comment.dto



data class UpdateCommentRequest(
    val description: String?,
    val writer: String?,
    val password: String?
)