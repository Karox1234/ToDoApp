package com.teamsparta.todoapp.domain.comment.service


import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment

interface CommentService {
    fun createComment(cardId: Long, request: CreateCommentRequest,userId:Long): Comment

    fun updateComment(commentId: Long, request: UpdateCommentRequest, userId:Long): CommentResponse

    fun deleteComment(commentId: Long,userId:Long)

    fun getCommentsByCard(cardId: Long): List<CommentResponse>
}