package com.teamsparta.todoapp.domain.comment.service


import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest

interface CommentService {
    //    fun getAllCardList(): List<CardResponse>
    //TODO:컨트롤러와 마찬가지 이유로 일단 보류
    fun getCommentById(commentId: Long): CommentResponse

    fun createComment(request: CreateCommentRequest): CommentResponse

    fun updateComment(commentId: Long, request: UpdateCommentRequest): CommentResponse

    fun deleteComment(commentId: Long)

}