package com.teamsparta.todoapp.domain.user.model


import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.comment.model.Comment
import com.teamsparta.todoapp.domain.user.dto.UserResponse
import jakarta.persistence.*
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited

@Audited
@Entity
@Table(name = "app_user")
class UserEntity(
    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Embedded
    var profile: Profile,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @Column(name = "card_count", nullable = false)
    var cardCount: Int = 0,

    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val comments: List<Comment> = mutableListOf(),

    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val cards: List<Card> = mutableListOf()


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun UserEntity.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        nickname = profile.nickname,
        email = email,
        role = role.name
    )
}