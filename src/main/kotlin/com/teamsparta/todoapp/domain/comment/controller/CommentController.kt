package com.teamsparta.todoapp.domain.comment.controller

import com.teamsparta.todoapp.domain.cards.dto.CardResponse
import com.teamsparta.todoapp.domain.cards.dto.CreateCardRequest
import com.teamsparta.todoapp.domain.cards.dto.UpdateCardRequest
import com.teamsparta.todoapp.domain.cards.service.CardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comment")
@RestController
//TODO: 카드 컨트롤러를 뼈대로 수정하면서 필요한 부분,필요없는 부분 생성 및 삭제
class CommentController(private val commentService: CommentService)
{

    @GetMapping()
    fun getCardList(): ResponseEntity<List<CardResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK).body(commentService.getAllCardList())

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