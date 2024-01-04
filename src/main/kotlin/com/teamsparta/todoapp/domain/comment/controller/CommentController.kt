package com.teamsparta.todoapp.domain.comment.controller

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

class CommentController(private val commentService: CommentService) {

    @PostMapping("/{cardId}")
    fun createComment(
        @PathVariable cardId: Long, @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val createdComment: Comment = commentService.createComment(cardId, createCommentRequest)
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


    @DeleteMapping("/{cardId}/{commentId}")
    fun deleteComment(
        @PathVariable cardId: Long, @PathVariable commentId: Long, @RequestParam password: String
    ): ResponseEntity<String> {
        commentService.deleteComment(cardId, commentId, password)
        val successMessage = "댓글 삭제 완료"
        return ResponseEntity.status(HttpStatus.OK).body(successMessage)
    }


}

