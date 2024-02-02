package com.teamsparta.todoapp.domain.cards.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.user.model.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime



@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "card")
class Card(
    @Column(name = "title", nullable = false) var title: String,

    @Column(name = "description") var description: String? = null,

    @Column(name = "writer") var writer: String? = null,

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = OffsetDateTime.now(),

    @Column(name = "completed") var completed: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: UserEntity

    )
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Card.toResponse(): CardResponse {
    return CardResponse(
        id = id!!,
        title = title,
        description = description,
        writer = user.profile.nickname,
        createdAt = createdAt,
        userId = user.id!!,
        completed = completed
    )
}