package com.teamsparta.todoapp.domain.comment.controller

import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.service.CommentService
import com.teamsparta.todoapp.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/comments")
@RestController

class CommentController(private val commentService: CommentService) {

    @GetMapping("/{cardId}")
    fun getCommentsByPost(@PathVariable cardId: Long): ResponseEntity<List<CommentResponse>> {
        val comments = commentService.getCommentsByCard(cardId)
        return ResponseEntity.ok(comments)
    }

    @PostMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun createComment(
        @PathVariable cardId: Long, @RequestBody createCommentRequest: CreateCommentRequest, @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        val userId = user.id
        val createdComment: Comment = commentService.createComment(cardId, createCommentRequest,userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment.toResponse())
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest,
        @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<CommentResponse> {
        val userId = user.id
        val updatedComment = commentService.updateComment(commentId, updateCommentRequest, userId)
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment)
    }


    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun deleteComment(
        @PathVariable commentId: Long, @AuthenticationPrincipal user: UserPrincipal,
    ): ResponseEntity<String> {
        val userId = user.id
        commentService.deleteComment(commentId,userId)
        val successMessage = "댓글 삭제 완료"
        return ResponseEntity.status(HttpStatus.OK).body(successMessage)
    }


}

