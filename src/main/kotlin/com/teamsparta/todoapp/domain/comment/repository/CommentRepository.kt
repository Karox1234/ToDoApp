package com.teamsparta.todoapp.domain.comment.repository


import com.teamsparta.todoapp.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {

}