package com.teamsparta.todoapp.domain.cards.model

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.image.model.Image
import com.teamsparta.todoapp.domain.user.model.UserEntity
import com.teamsparta.todoapp.infra.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.envers.Audited

@Audited
@Entity
@Table(name = "card")
class Card(
    @Column(name = "title", nullable = false) var title: String,

    @Column(name = "description") var description: String? = null,

    @Column(name = "writer") var writer: String,

    @Column(name = "completed") var completed: Boolean = false,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: UserEntity,

    @Column
    var imageUrl: String?,

    @OneToMany(mappedBy = "card", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableList<Image> = mutableListOf()

) : BaseTimeEntity() {
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
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userId = user.id!!,
        completed = completed,
        imageUrl = imageUrl
    )
}