package com.teamsparta.todoapp.domain.comment.model


import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import jakarta.persistence.*


@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "description")
    var description: String? = null,

    @Column(name = "writer")
    var writer: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @ManyToOne
    @JoinColumn(name = "cardId")
    val card: Card

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