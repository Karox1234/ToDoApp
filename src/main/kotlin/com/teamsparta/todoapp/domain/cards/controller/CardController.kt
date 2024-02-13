package com.teamsparta.todoapp.domain.cards.controller

import com.teamsparta.todoapp.domain.cards.dto.*
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.cards.service.CardService
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RequestMapping("/cards")
@RestController
class CardController(private val cardService: CardService , private val cardRepository: CardRepository) {

    @GetMapping("/page")
    fun getCardPage(
        @RequestParam("pageNumber", defaultValue = "1") pageNumber: Int,
        @RequestParam("pageSize", defaultValue = "5") pageSize: Int,
        @RequestParam("sortField",required = false, defaultValue = "title") sortField: String?,
        @RequestParam("sortOrder") sortOrder:Sort.Direction
    ): ResponseEntity<CardPageResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardPage(pageNumber,pageSize,sortField,sortOrder))
    }

    @GetMapping
    fun getCardList(): ResponseEntity<List<CardResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardList())

    }

    @GetMapping("/{cardId}")
    fun getCard(
        @PathVariable cardId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<CardAndCommentPagingResponse> {
        val cardResponse = cardService.getCardById(cardId, page, size)
        return ResponseEntity.ok(cardResponse)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createCard(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestParam("imageFile") imageFile: MultipartFile?,
        @RequestParam("title") title: String,
        @RequestParam("description") description: String?,
    ): ResponseEntity<CardResponse> {
        val createCardRequest = CreateCardRequest(title, description, imageFile)
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(createCardRequest, user.id))
    }

    @PutMapping("/{cardId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateCard(
        @PathVariable cardId: Long,
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestParam("imageFile") imageFile: MultipartFile?,
        @RequestParam("title") title: String,
        @RequestParam("description") description: String?,
        @Parameter(description = "사진 변경 없이 기존 사진을 삭제하고 싶을시 true로 작동해주세요") @RequestParam("delOldImageUrl", required = false, defaultValue = "false") delOldImageUrl: Boolean
    ): ResponseEntity<CardResponse> {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        val oldImageUrl = card.imageUrl
        val updateCardRequest = UpdateCardRequest(title, description, imageFile, oldImageUrl,delOldImageUrl)
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(cardId, user.id ,updateCardRequest))
    }

    @DeleteMapping("/{cardId}")
    fun deleteCard(@PathVariable cardId: Long,@AuthenticationPrincipal user: UserPrincipal): ResponseEntity<Unit> {
        val userId= user.id
        cardService.deleteCard(cardId,userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()

    }

    @PatchMapping("/{cardId}/toggle")
    fun toggleCardCompletion(@PathVariable cardId: Long,@AuthenticationPrincipal user: UserPrincipal): ResponseEntity<CardResponse> {
        val userId= user.id
        return ResponseEntity.status(HttpStatus.OK).body(cardService.toggleCardCompletion(cardId,userId))
    }

    @GetMapping("/{cardId}/comments")
    fun testCardAndCommentGetList(@PathVariable cardId: Long): ResponseEntity<CardAndCommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.testCardAndCommentGetList(cardId))
    }
}
