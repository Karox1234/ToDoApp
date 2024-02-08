package com.teamsparta.todoapp.domain.scheduler

import com.teamsparta.todoapp.domain.scheduler.service.VerificationDeleteService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class VerificationDeleteScheduler(
    private val verificationDeleteService: VerificationDeleteService
) {

    //10분마다 실행
    @Scheduled(initialDelay = 10 * 60 * 1000 , fixedRate = 10 * 60 * 1000)
    fun deleteExpiredVerifications() {
        verificationDeleteService.deleteExpiredVerifications()
    }
}

//전용포트나 전용서버로 스케쥴러를 따로 하는게 좋다. 소프트 딜리트. 프린트ln보단 로그를 남겨보자->로그를 db에저장(스프링 로그4J)