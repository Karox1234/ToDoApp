package com.teamsparta.todoapp.domain.comment.service

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.service.CardServiceImpl
import com.teamsparta.todoapp.domain.comment.controller.toCard
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.repository.CommentRepository
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service

class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val cardService: CardServiceImpl
) : CommentService {


    override fun getCommentById(commentId: Long): CommentResponse {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return comment.toResponse()
    }
    @Transactional
    override fun createComment(card: Card, request: CreateCommentRequest): Comment {
        val cardResponse: CardResponse = cardService.getCardById(card.id!!)
        val card: Card = cardResponse.toCard()
        val comment = Comment(
            card = card,
            description = request.description,
            writer = request.writer,
            password = request.password
        )

        return commentRepository.save(comment)
    }
    @Transactional
    override fun updateComment(commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        val (description, writer) = request

        comment.description = description
        comment.writer = writer

        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    override fun deleteComment(commentId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        commentRepository.delete(comment)
    }
}

fun CardResponse.toCard(): Card {
    return Card(id = this.id, title = this.title, description = this.description,
        writer = this.writer, createdAt = this.createdAt, completed = this.completed ?:false)
}