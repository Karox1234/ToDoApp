package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.*
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.toResponse
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.exception.CardOverException
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.teamsparta.todoapp.domain.user.model.UserRole
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.AccessDeniedException


@Service

class CardServiceImpl(
    private val cardRepository: CardRepository, private val userRepository: UserRepository
) : CardService {

    override fun getCardPage(pageNumber: Int, pageSize: Int, sortField: String?, sortOrder: Sort.Direction): CardPageResponse {

        val pageable: PageRequest = PageRequest.of(pageNumber -1, pageSize,sortOrder,sortField)
        val cardPage: Page<Card> = cardRepository.findAll(pageable)

        val cardResponseList: List<CardResponse> = cardPage.content.map { it.toResponse() }

        return CardPageResponse(
            content = cardResponseList,
            totalPages = cardPage.totalPages,
            totalCards = cardPage.totalElements
        )
    }




//    override fun getAllCardList(): List<CardResponse> {
//        return cardRepository.findAll().map { it.toResponse() }
//    } 그냥 조회

//    override fun getAllCardList(): List<CardResponse> = cardRepository.findAll().sortedByDescending {it.createdAt}.map {it.toResponse()}
//           생성시간순 내림차순 조회 구현

    override fun getAllCardList(): List<CardResponse> {
        val sort: Sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val cards: List<Card> = cardRepository.findAll(sort)
        return cards.map { it.toResponse() }
    }
    //n+1해결을 위한 로직 변경


    override fun getCardById(cardId: Long): CardResponse =
        cardRepository.findByIdOrNull(cardId)?.toResponse() ?: throw ModelNotFoundException("Card",cardId)


    @Transactional
    override fun createCard(request: CreateCardRequest, userId: Long): CardResponse =
        userRepository.findByIdOrNull(userId)?.let { user ->
            if (user.role == UserRole.USER && user.cardCount >= 3) {
                throw CardOverException("유저는 카드를 세개 이상 만들수 없습니다.")
            }

            val savedCard = cardRepository.save(
                Card(
                    title = request.title,
                    description = request.description,
                    user = user,
                    writer = user.profile.nickname
                )
            )

            user.cardCount++

            savedCard.toResponse()
        } ?: throw ModelNotFoundException("User", userId)


    @Transactional
    override fun updateCard(cardId: Long, userId: Long, request: UpdateCardRequest): CardResponse =
        cardRepository.findByIdOrNull(cardId)?.let { card ->
            if (card.user.id != userId) {
                throw AccessDeniedException("You do not have permission to update this card.")
            }
            with(request) {
                card.title = title
                card.description = description
            }
            cardRepository.save(card).toResponse()
        } ?: throw ModelNotFoundException("Card", cardId)


    @Transactional
    override fun deleteCard(cardId: Long, userId: Long) {
        userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        cardRepository.findByIdOrNull(cardId)?.let { card ->
            if (card.user.id != userId) {
                throw AccessDeniedException("You do not have permission to delete this card.")
            }

            userRepository.findByIdOrNull(userId)?.let { user ->
                user.cardCount--
            }

            cardRepository.delete(card)
        } ?: throw ModelNotFoundException("Card", cardId)
    }

    @Transactional
    override fun toggleCardCompletion(cardId: Long, userId: Long): CardResponse =
        cardRepository.findByIdOrNull(cardId)?.let { card ->
            if (card.user.id != userId) {
                throw AccessDeniedException("You do not have permission to toggle this card.")
            }
            card.completed = !card.completed
            card.toResponse()
        } ?: throw ModelNotFoundException("Card", cardId)


}



