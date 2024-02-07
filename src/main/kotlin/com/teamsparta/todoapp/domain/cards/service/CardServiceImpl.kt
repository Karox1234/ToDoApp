package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.*
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.toResponse
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.exception.CardOverException
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import com.teamsparta.todoapp.domain.image.model.Image
import com.teamsparta.todoapp.domain.image.repository.ImageRepository
import com.teamsparta.todoapp.domain.image.service.ImageService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.teamsparta.todoapp.domain.user.model.UserRole
import com.teamsparta.todoapp.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@Service

class CardServiceImpl(
    private val cardRepository: CardRepository, private val userRepository: UserRepository , private val imageRepository: ImageRepository , private val imageService: ImageService
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
    override fun createCard(request: CreateCardRequest, userId: Long): CardResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("User", userId)
        if (user.role == UserRole.USER && user.cardCount >= 3) {
            throw CardOverException("유저는 카드를 세개 이상 만들 수 없습니다.")
        }

        //이미지를 넣을지 말지 결정하는 로직
        val imageUrl = if (request.imageUrl != null) {
            imageService.uploadImage(request.imageUrl)
            //null 이 아닌경우, image서비스 로직을 불러와서 적용
        } else {
            //아닌경우 null
            null
        }

        val savedCard = cardRepository.save(
            Card(
                title = request.title,
                description = request.description,
                user = user,
                writer = user.profile.nickname,
                imageUrl = imageUrl //위에서 정한대로 진행 하면서 저장
            )
        )
        //이미지가 있는경우
        if (imageUrl != null) {
            //이미지 레퍼지토리에 저장함(글에 올라간 사진에 대해서 데이터를 저장함)
            imageRepository.save(
                Image(
                    fileName = savedCard.imageUrl!!,
                    url = imageUrl,
                    card = savedCard
                )
            )
        }

        user.cardCount++
        return savedCard.toResponse()
    }



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

