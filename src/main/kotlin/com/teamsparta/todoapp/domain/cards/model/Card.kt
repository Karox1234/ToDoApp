package com.teamsparta.todoapp.domain.cards.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "card")
class Card(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description")
    var description: String? = null,


    @Column(name = "writer")
    var writer: String? = null,

    @Column(name = "createdAt")
    var createdAt: OffsetDateTime


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null



}
fun Card.toResponse(): CardResponse {
    return CardResponse(
        id = id!!,
        title = title,
        description = description,
        writer = writer,
        createdAt = createdAt
    )
}