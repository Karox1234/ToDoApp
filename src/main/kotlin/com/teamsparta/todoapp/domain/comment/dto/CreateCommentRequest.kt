package com.teamsparta.todoapp.domain.comment.dto


data class CreateCommentRequest(
    val description: String?, val writer: String?, val password: String?
)

