package com.teamsparta.todoapp.domain.user.scheduler

import com.teamsparta.todoapp.domain.user.service.VerificationDeleteService
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
        println("만료된 이메일 인증정보를 삭제하였습니다")
    }
}