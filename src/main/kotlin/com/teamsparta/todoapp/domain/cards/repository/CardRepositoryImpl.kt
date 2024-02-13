package com.teamsparta.todoapp.domain.cards.repository


import com.teamsparta.todoapp.domain.cards.dto.CardAndCommentPagingResponse
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.QCard
import com.teamsparta.todoapp.domain.comment.model.QComment
import com.teamsparta.todoapp.infra.QueryDslSupport
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CardRepositoryImpl : CustomCardRepository, QueryDslSupport() {

    private val card = QCard.card
    private val comment = QComment.comment

    override fun findCardWithComments(cardId: Long): Card? {
        return queryFactory
            .selectFrom(card)
            .leftJoin(card.comments, comment).fetchJoin()
            .leftJoin(comment.user).fetchJoin()
            .where(card.id.eq(cardId))
            .fetchOne()
    }

    override fun findCardWithPagedComments(cardId: Long, pageable: Pageable): CardAndCommentPagingResponse {
        TODO("쿼리로 페이징 만들어 보기")
    }
}

