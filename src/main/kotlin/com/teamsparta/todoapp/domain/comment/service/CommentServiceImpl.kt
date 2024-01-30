package com.teamsparta.todoapp.domain.comment.service

import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.repository.CommentRepository
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.user.model.UserEntity
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service


@Service

class CommentServiceImpl(
    private val commentRepository: CommentRepository, private val cardRepository: CardRepository,private val userRepository: UserRepository
) : CommentService {

    @Transactional
    override fun createComment(cardId: Long, request: CreateCommentRequest,userId:Long): Comment {
        val card: Card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        val user: UserEntity = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        val comment = Comment(
            card = card, user = user, description = request.description)

        return commentRepository.save(comment)
    }


    @Transactional
    override fun updateComment(
        commentId: Long, request: UpdateCommentRequest, userId: Long
    ): CommentResponse {
        val checkComment: Comment = commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("Comment", commentId)
        if (checkComment.user.id != userId) {
            throw AccessDeniedException("You do not have permission to update this comment.")
        }
        checkComment.description = request.description
        val updatedComment: Comment = commentRepository.save(checkComment)
        return updatedComment.toResponse()
    }


    @Transactional
    override fun deleteComment(commentId: Long,userId:Long) {
        val deleteComment: Comment = commentRepository.findByIdOrNull(commentId)
            ?: throw ModelNotFoundException("Comment", commentId)
        if (deleteComment.user.id != userId) {
            throw AccessDeniedException("You do not have permission to delete this comment.")
        }
        commentRepository.delete(deleteComment)
    }

    override fun getCommentsByCard(cardId:Long): List<CommentResponse> {
        val comments = commentRepository.getCommentsByCardId(cardId)
        return comments.map { it.toResponse() }
    }
}

