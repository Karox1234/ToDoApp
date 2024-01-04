package com.teamsparta.todoapp.domain.comment.dto

import com.fasterxml.jackson.annotation.JsonIgnore


data class UpdateCommentRequest(
    val description: String?, @JsonIgnore val password: String?, @JsonIgnore val writer: String?
)