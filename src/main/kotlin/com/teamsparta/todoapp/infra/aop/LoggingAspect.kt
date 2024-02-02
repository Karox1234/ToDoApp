package com.teamsparta.todoapp.infra.aop

import com.teamsparta.todoapp.domain.cards.repository.CardRepository
import com.teamsparta.todoapp.domain.exception.ModelNotFoundException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Aspect //Aspect 선언
@Component //Bean으로 등록될수 있게 하는 어노테이션
class LoggingAspect(private val cardRepository: CardRepository) {

    @Before("execution(* com.teamsparta.todoapp.domain.cards.service.CardServiceImpl.updateCard(..))")
    //updatecard 메서드 시작전에 시작하라는 포인트컷
    fun logBeforeUpdateCard(joinPoint: JoinPoint) {
        val checkOriginalCardArgs = joinPoint.signature.name //실행중인 메서드의 정보에 접근해서, 이름을 가져옴

        // 메소드의 인자들을 추출
        val args = joinPoint.args
        val cardId = args[0] as Long
        //기존 카드의 정보를 가져오기 위한 id검색
        val originalCard = cardRepository.findByIdOrNull(cardId) ?: throw ModelNotFoundException("Card", cardId)

        //출력되는 결과물
        println("$checkOriginalCardArgs 전 원래 카드 정보를 체크합니다 ") //업데이트 카드 전 원래 정보를 체크합니다 ( 이름을 가져왔기 때문에 이렇게 출력)
        println("원래 정보, 제목: ${originalCard.title}, 내용: ${originalCard.description}")
    }
}