package com.teamsparta.todoapp.domain.scheduler

import com.teamsparta.todoapp.domain.scheduler.service.DeleteCardUpdateHistoryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CardUpdateHistoryDeleteScheduler(private val deleteCardUpdateHistoryService : DeleteCardUpdateHistoryService) {

    @Scheduled(initialDelay = 10 * 60 * 1000 , fixedRate = 24 * 60 * 60 * 1000) // 하루 마다 실행
    fun deleteCardUpdateHistory() {
        deleteCardUpdateHistoryService.deleteCardUpdateHistory()
    }

}
