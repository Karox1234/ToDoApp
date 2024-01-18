package com.teamsparta.todoapp.domain.comment.dto



data class CommentResponse(
    val id: Long,
    val description: String?,
    val writer: String,
    val userId:Long,
    val cardId:Long
)
