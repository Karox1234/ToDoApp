package com.teamsparta.todoapp.domain.comment.service

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.model.Card
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
    private val commentRepository: CommentRepository
) : CommentService {

    @Transactional
    fun checkCommentAndPassword(cardId: Long, commentId: Long, password: String): Comment {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.card.id != cardId) {
            throw IllegalArgumentException("CardId에 맞는 CommentID가 not found")
        }

        if (password != comment.password) {
            throw IllegalArgumentException("password error")
        }

        return comment
    }

    @Transactional
    override fun createComment(cardId: Long, card: Card, request: CreateCommentRequest): Comment {

        val comment = Comment(
            card = card, description = request.description, writer = request.writer, password = request.password
        )

        return commentRepository.save(comment)
    }

    @Transactional
    override fun updateComment(
        cardId: Long, commentId: Long, request: UpdateCommentRequest, password: String
    ): CommentResponse {
        val comment = checkCommentAndPassword(cardId, commentId, password)
        comment.description = request.description
        return commentRepository.save(comment).toResponse()
    }


    @Transactional
    override fun deleteComment(cardId: Long, commentId: Long, password: String) {
        val comment = checkCommentAndPassword(cardId, commentId, password)
        commentRepository.delete(comment)
    }
}

fun CardResponse.toCard(): Card {
    return Card(
        id = this.id,
        title = this.title,
        description = this.description,
        writer = this.writer,
        createdAt = this.createdAt,
        completed = this.completed ?: false
    )
}

