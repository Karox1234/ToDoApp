package com.teamsparta.todoapp.domain.comment.dto



data class CommentResponse(
    val id: Long,
    val description: String?,
    val writer: String?,

)
