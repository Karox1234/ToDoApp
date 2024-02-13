package com.teamsparta.todoapp.domain.cards.repository

import com.teamsparta.todoapp.domain.cards.dto.CardAndCommentPagingResponse
import com.teamsparta.todoapp.domain.cards.model.Card
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository



interface CardRepository : JpaRepository<Card, Long> {

    @EntityGraph(attributePaths = ["user"])
    override fun findAll(pageable: Pageable): Page<Card>

    @EntityGraph(attributePaths = ["user"])
    override fun findAll(sort: Sort): MutableList<Card>

    fun findCardWithComments(cardId: Long): Card?

    fun findCardWithPagedComments(cardId: Long, pageable: Pageable): CardAndCommentPagingResponse

}