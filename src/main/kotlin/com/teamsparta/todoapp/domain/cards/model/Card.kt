package com.teamsparta.todoapp.domain.cards.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.comment.model.Comment
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "card")
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "writer")
    var writer: String? = null,

    @Column(name = "createdAt")
    var createdAt: OffsetDateTime,

    @Column(name = "completed")
    var completed: Boolean = false,

    @OneToMany(mappedBy = "card", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf()

)


fun Card.toResponse(): CardResponse {
    return CardResponse(
        id = id!!,
        title = title,
        description = description,
        writer = writer,
        createdAt = createdAt,
        completed = completed
    )
}