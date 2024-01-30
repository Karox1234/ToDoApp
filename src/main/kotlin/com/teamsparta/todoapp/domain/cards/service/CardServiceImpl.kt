package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.toResponse
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.teamsparta.todoapp.domain.user.model.UserEntity
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import org.springframework.security.access.AccessDeniedException

@Service

class CardServiceImpl(
    private val cardRepository: CardRepository, private val userRepository: UserRepository
) : CardService {

    override fun getAllCardList(): List<CardResponse> {
        return cardRepository.findAll().map { it.toResponse() }
    }

    override fun getCardById(cardId: Long): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        return card.toResponse()

    }

    @Transactional
    override fun createCard(request: CreateCardRequest,userId: Long): CardResponse {
        val user : UserEntity = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        if (user.cardCount >= 3) {
            throw Exception("User can have a maximum of 3 cards.")
        } //customExeption을 만들기 위한 상황 추가
        val savedCard = cardRepository.save(
            Card(
                title = request.title,
                description = request.description,
                user = user
            )
        )

        user.cardCount++

        return savedCard.toResponse()
    }



    @Transactional
    override fun updateCard(cardId: Long, userId: Long, request: UpdateCardRequest): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        //카드 작성 유저와 현재 유저가 같은지 검증과정
        if (card.user.id != userId) {
            throw AccessDeniedException("You do not have permission to update this card.")
            //AccessDeniedException은 customAccessDeniedHandler에서 따로 조절
        }
        val (title, description) = request
        card.title = title
        card.description = description

        val updatedCard = cardRepository.save(card)
        return updatedCard.toResponse()
    }

    @Transactional
    override fun deleteCard(cardId: Long,userId: Long) {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        if (card.user.id != userId) {
            throw AccessDeniedException("You do not have permission to delete this card.")
        }
        cardRepository.delete(card)
    }

    @Transactional
    override fun toggleCardCompletion(cardId: Long,userId: Long): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        if (card.user.id != userId) {
            throw AccessDeniedException("You do not have permission to toggle this card.")
        }
        card.completed = !card.completed
        return card.toResponse()
    }


}



