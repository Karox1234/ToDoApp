package com.teamsparta.todoapp.domain.scheduler

import com.teamsparta.todoapp.domain.scheduler.service.DeleteCardUpdateHistoryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CardUpdateHistoryDeleteScheduler(private val deleteCardUpdateHistoryService : DeleteCardUpdateHistoryService) {

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 하루 마다 실행
    fun deleteCardUpdateHistory() {
        deleteCardUpdateHistoryService.deleteCardUpdateHistory()
    }

}
//TODO:service로직 완성되면 다른 포트로 돌리는법도 연구해보기, initialDelay 걸기