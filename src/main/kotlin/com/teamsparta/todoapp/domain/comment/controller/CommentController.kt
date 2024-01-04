package com.teamsparta.todoapp.domain.comment.controller

import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.service.CardService
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.service.CommentService
import com.teamsparta.todoapp.domain.comment.service.toCard
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comment")
@RestController

class CommentController(private val commentService: CommentService, private val cardService: CardService) {

    @PostMapping("/{cardId}")
    fun createComment(
        @PathVariable cardId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val card: Card = cardService.getCardById(cardId).toCard()
        val createdComment: Comment = commentService.createComment(cardId, card, createCommentRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment.toResponse())
    }


    @PutMapping("/{cardId}/{commentId}")
    fun updateComment(
        @PathVariable cardId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest,
        @RequestParam password: String
    ): ResponseEntity<CommentResponse> {
        val updatedComment = commentService.updateComment(cardId, commentId, updateCommentRequest, password)
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment)
    }


    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()

    }

}

