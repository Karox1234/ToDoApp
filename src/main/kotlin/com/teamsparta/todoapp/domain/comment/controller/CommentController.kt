package com.teamsparta.todoapp.domain.comment.controller


import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.service.CardService
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comment")
@RestController

class CommentController(private val commentService: CommentService, private val cardService: CardService) {

    @GetMapping("/{commentId}")
    fun getComment(@PathVariable commentId: Long): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(commentId))

    }

    @PostMapping("/{cardId}")
    fun createComment(
        @PathVariable cardId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val cardResponse: CardResponse = cardService.getCardById(cardId)
        val card: Card = cardResponse.toCard()
        val createdComment: Comment = commentService.createComment(card, createCommentRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment.toResponse())
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long, @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, updateCommentRequest))

    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()

    }

}

fun CardResponse.toCard(): Card {
    return Card(id = this.id, title = this.title, description = this.description,
        writer = this.writer, createdAt = this.createdAt, completed = this.completed ?:false)
}
