package com.teamsparta.todoapp.domain.comment.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "description")
    var description: String? = null,

    @Column(name = "writer")
    var writer: String? = null,

    @Column(name = "password")
    var password: String? = null,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        id = id!!,
        description = description,
        writer = writer,
    )
}