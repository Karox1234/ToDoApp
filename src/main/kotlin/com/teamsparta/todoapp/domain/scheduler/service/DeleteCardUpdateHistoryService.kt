package com.teamsparta.todoapp.domain.scheduler.service

import com.teamsparta.todoapp.infra.RevisionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DeleteCardUpdateHistoryService(private val revisionRepository: RevisionRepository) {

    @Transactional
    fun deleteCardUpdateHistory() {
        val currentTimestamp = LocalDateTime.now()
        val timestamp90DaysAgo = currentTimestamp.minusDays(90)

        revisionRepository.deleteByTimestampBefore(timestamp90DaysAgo)
    }
}

