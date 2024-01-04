package com.teamsparta.todoapp.domain.comment.service


import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment

interface CommentService {
    fun createComment(cardId:Long, card: Card, request: CreateCommentRequest): Comment

    fun updateComment(cardId: Long, commentId: Long, request: UpdateCommentRequest, password: String): CommentResponse

    fun deleteComment(commentId: Long)

}