package com.teamsparta.todoapp.domain.cards.service

import com.teamsparta.todoapp.domain.cards.dto.*
import com.teamsparta.todoapp.domain.cards.model.Card
import com.teamsparta.todoapp.domain.cards.model.toCardAndCommentResponse
import com.teamsparta.todoapp.domain.cards.model.toResponse
import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.comment.model.toResponse
import com.teamsparta.todoapp.domain.comment.repository.CommentRepository
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

@Service

class CardServiceImpl(
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
    private val imageService: ImageService,
    private val commentRepository: CommentRepository
) : CardService {

    override fun getCardPage(
        pageNumber: Int,
        pageSize: Int,
        sortField: String?,
        sortOrder: Sort.Direction
    ): CardPageResponse {

        val pageable: PageRequest = PageRequest.of(pageNumber - 1, pageSize, sortOrder, sortField)
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


    @Transactional
    override fun getCardById(cardId: Long, page: Int, size: Int): CardAndCommentPagingResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)

        val pageable = PageRequest.of(page-1, size)
        val pagedComments = commentRepository.findByCardId(cardId, pageable)

        val commentResponses = pagedComments.content.map { it.toResponse() }

        val cardResponse = card.toCardAndCommentResponse(commentResponses)
        return CardAndCommentPagingResponse(
            content = listOf(cardResponse),
            totalPages = pagedComments.totalPages,
            totalComments = pagedComments.totalElements
        )
    }


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
            val fileName = imageUrl.substringAfterLast("/")
            //이미지 레퍼지토리에 저장함(글에 올라간 사진에 대해서 데이터를 저장함)
            imageRepository.save(
                Image(
                    fileName = fileName,
                    url = imageUrl,
                    card = savedCard
                )
            )
        }

        user.cardCount++
        return savedCard.toResponse()
    }

    @Transactional
    override fun updateCard(cardId: Long, userId: Long, request: UpdateCardRequest): CardResponse {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)
        if (card.user.id != userId) {
            throw AccessDeniedException("You do not have permission to update this card.")
        }

        val title = request.title
        val description = request.description

        val newImageUrl = if (request.imageUrl != null) {
            imageService.uploadImage(request.imageUrl)
        } else {
            null
        }

        //기존 이미지 Url 가져오기
        val oldImageUrl = request.oldImageUrl

        //새로운 이미지가 있고, 기존 이미지가 있는 경우
        if (newImageUrl != null && oldImageUrl != null) {
            // 새로운 이미지 Url과 기존 이미지 Url이 다를 경우에만 새로운 이미지 저장
            if (newImageUrl != oldImageUrl) {
                val fileName = newImageUrl.substringAfterLast("/")
                imageRepository.save(
                    Image(
                        fileName = fileName, url = newImageUrl, card = card
                    )
                )
                //새로운 이미지 Url이 있는 경우에만 카드의 이미지 Uru을 업데이트
                card.imageUrl = newImageUrl
                //기존 이미지 Url 삭제
                val oldImage = imageRepository.findByUrl(oldImageUrl)
                imageRepository.delete(oldImage)
            }
        }
        //새로운 이미지가 있고, 기존 이미지가 없는 경우(기존 추가 로직)
        else if (newImageUrl != null) {
            val fileName = newImageUrl.substringAfterLast("/")
            imageRepository.save(
                Image(
                    fileName = fileName, url = newImageUrl, card = card
                )
            )//새로운 이미지 Url이 있는 경우에만 카드의 이미지 Uru을 업데이트
            card.imageUrl = newImageUrl
        }

        //기존에 Url이 있는데,
        if (oldImageUrl != null) {
            //새로운 이미지가 없는 경우(이미지가 변경되지 않은 경우)
            if (newImageUrl == null) {
                // 기존 URL을 그대로 유지
                card.imageUrl = oldImageUrl
            }
            if (request.delOldImageUrl) {
                //기존 이미지를 삭제하고 싶은경우(이미지 교체가 아닌, 단순 삭제)
                val oldImage = imageRepository.findByUrl(oldImageUrl)
                imageRepository.delete(oldImage)
                card.imageUrl = null
            }
        }

        card.title = title
        card.description = description

        return card.toResponse()
    }

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

