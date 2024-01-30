package com.teamsparta.todoapp.domain.comment.model


import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.user.model.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction


@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "description") var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_Id")
    @OnDelete(action = OnDeleteAction.CASCADE)
     val card: Card,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: UserEntity

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        id = id!!,
        description = description,
        writer = user.profile.nickname,
        cardId = card.id!!,
        userId = user.id!!
    )
}