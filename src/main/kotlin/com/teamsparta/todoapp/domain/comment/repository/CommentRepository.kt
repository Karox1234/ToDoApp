package com.teamsparta.todoapp.domain.comment.repository


import com.teamsparta.todoapp.domain.comment.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun getCommentsByCardId(cardId: Long): List<Comment>

    fun findByCardId(cardId: Long, pageable: Pageable): Page<Comment>
}