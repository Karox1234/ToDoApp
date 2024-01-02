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
import java.time.OffsetDateTime

@Service

class CardServiceImpl(
    private val cardRepository: CardRepository
) : CardService {

    override fun getAllCardList(): List<CardResponse> {
        return cardRepository.findAll().map { it.toResponse() }
    }

    override fun getCardById(cardId: Long): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        return card.toResponse()
    }

    @Transactional
    override fun createCard(request: CreateCardRequest): CardResponse {
        return cardRepository.save(
            Card(
                title = request.title,
                description = request.description,
                writer = request.writer,
                createdAt = OffsetDateTime.now(),
            )
        ).toResponse()
    }

    @Transactional
    override fun updateCard(cardId: Long, request: UpdateCardRequest): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        val (title, description, writer) = request

        card.title = title
        card.description = description
        card.writer = writer

        return cardRepository.save(card).toResponse()
    }

    @Transactional
    override fun deleteCard(cardId: Long) {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        cardRepository.delete(card)
    }

    @Transactional
    override fun toggleCardCompletion(cardId: Long): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        card.completed = !card.completed
        return card.toResponse()
    }
}