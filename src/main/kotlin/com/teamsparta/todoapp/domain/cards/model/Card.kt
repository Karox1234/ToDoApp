package com.teamsparta.todoapp.domain.cards.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime



@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "card")
class Card(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @Column(name = "title", nullable = false) var title: String,

    @Column(name = "description") var description: String? = null,

    @Column(name = "writer") var writer: String? = null,

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = OffsetDateTime.now(),

    @Column(name = "completed") var completed: Boolean = false,


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