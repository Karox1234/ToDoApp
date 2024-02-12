package com.teamsparta.todoapp.domain.cards.repository

import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.QCard
import com.teamsparta.todoapp.domain.comment.model.QComment
import com.teamsparta.todoapp.infra.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class CardRepositoryImpl : CustomCardRepository ,QueryDslSupport() {

    private val card = QCard.card
    private val comment = QComment.comment
//적용한 곳 없음. 단순 연습
    override fun findCardWithComments(cardId: Long): Card? {
        return queryFactory
            .selectFrom(card)
            .leftJoin(card.comments, comment).fetchJoin()
            .where(card.id.eq(cardId))
            .fetchOne()
    }
}
