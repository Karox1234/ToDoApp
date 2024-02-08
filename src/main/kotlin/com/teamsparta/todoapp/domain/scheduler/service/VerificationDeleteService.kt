package com.teamsparta.todoapp.domain.scheduler.service

import com.teamsparta.todoapp.domain.user.repository.VerificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerificationDeleteService (
    private val verificationRepository: VerificationRepository
) {

    @Transactional
    fun deleteExpiredVerifications() {
        val allVerifications = verificationRepository.findAll() //모든 인증번호 조회
        allVerifications.forEach { verificationInfo -> //forEach를 사용하여 모든 데이터를 탐색함
            if (System.currentTimeMillis() > verificationInfo.expirationTime) { //그중 현재시간과 인증만료시간을 비교함
                verificationRepository.delete(verificationInfo) //해당 되는것들을 삭제
            }
        }
    }
}