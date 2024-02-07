package com.teamsparta.todoapp.domain.image.model

import com.teamsparta.todoapp.domain.cards.model.Card
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Table
@Entity
class Image(

    val fileName: String,

    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    val card: Card? = null


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}