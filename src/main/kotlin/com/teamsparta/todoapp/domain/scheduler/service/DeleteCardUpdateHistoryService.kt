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

        // 현재 시간보다 이전에 생성된 리비전 삭제
        revisionRepository.deleteByTimestampBefore(currentTimestamp)
    }
}
//원래 의도는 리비전info를 삭제하면서 연결된 aud들을 다 삭제 하려고 했는데, 키 제약 조건때문에 삭제가 안됨
//TODO: 키 제약 조건 해결후 삭제가 되면, 시간 조건 걸기(90일)


