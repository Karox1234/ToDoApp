package com.teamsparta.todoapp.domain.cards.controller

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest
import com.teamsparta.todoapp.domain.cards.service.CardService
import com.teamsparta.todoapp.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/cards")
@RestController
class CardController(private val cardService: CardService) {

    @GetMapping
    fun getCardList(): ResponseEntity<List<CardResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardList())

    }

    @GetMapping("/{cardId}")
    fun getCard(@PathVariable cardId: Long): ResponseEntity<CardResponse> {
        val cardResponse = cardService.getCardById(cardId)
        return ResponseEntity.status(HttpStatus.OK).body(cardResponse)
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun createCard(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestBody createCardRequest: CreateCardRequest
    ): ResponseEntity<CardResponse> {
        val userId = user.id
        val cardResponse = cardService.createCard(createCardRequest,userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponse)
    }

    @PutMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun updateCard(
        @PathVariable cardId: Long,
        @RequestBody updateCardRequest: UpdateCardRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<CardResponse> {
        val userId= user.id
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(cardId, userId ,updateCardRequest))
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun deleteCard(@PathVariable cardId: Long,@AuthenticationPrincipal user: UserPrincipal): ResponseEntity<Unit> {
        val userId= user.id
        cardService.deleteCard(cardId,userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()

    }

    @PatchMapping("/{cardId}/toggle")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun toggleCardCompletion(@PathVariable cardId: Long,@AuthenticationPrincipal user: UserPrincipal): ResponseEntity<CardResponse> {
        val userId= user.id
        return ResponseEntity.status(HttpStatus.OK).body(cardService.toggleCardCompletion(cardId,userId))
    }


}
