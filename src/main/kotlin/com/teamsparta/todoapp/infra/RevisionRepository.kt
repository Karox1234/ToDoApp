package com.teamsparta.todoapp.infra

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface RevisionRepository : JpaRepository<RevisionEntity,Long> {
    fun deleteByTimestampBefore(timestamp: LocalDateTime)
}