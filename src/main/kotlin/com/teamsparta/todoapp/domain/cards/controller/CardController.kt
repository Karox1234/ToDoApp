package com.teamsparta.todoapp.domain.cards.controller

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest
import com.teamsparta.todoapp.domain.cards.service.CardService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/card")
@RestController
class CardController(private val cardService: CardService)
{

    @GetMapping()
    fun getCardList(): ResponseEntity<List<CardResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK).body(cardService.getAllCardList())

    }

    @GetMapping("/{cardId}")
    fun getCard(@PathVariable cardId: Long): ResponseEntity<CardResponse> {
        return ResponseEntity
            .status(HttpStatus.OK).body(cardService.getCardById(cardId))

    }

    @PostMapping
    fun createCard(@RequestBody createCardRequest: CreateCardRequest): ResponseEntity<CardResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED).body(cardService.createCard(createCardRequest))

    }

    @PutMapping("/{cardId}")
    fun updateCard(
        @PathVariable cardId: Long,
        @RequestBody updateCardRequest: UpdateCardRequest
    ): ResponseEntity<CardResponse> {
        return ResponseEntity
            .status(HttpStatus.OK).body(cardService.updateCard(cardId,updateCardRequest))

    }

    @DeleteMapping("/{cardId}")
    fun deleteCard(@PathVariable cardId: Long): ResponseEntity<Unit> {
        cardService.deleteCard(cardId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT).build()

    }

}