package com.teamsparta.todoapp.domain.user.repository

import com.teamsparta.todoapp.domain.user.model.Verification
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationRepository : JpaRepository<Verification, Long> {
    fun findByEmailAndVerificationCode(email: String, verificationCode: Int): Verification?
}