package com.teamsparta.todoapp.infra

import jakarta.persistence.*
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
@RevisionEntity
@Table(name = "REVINFO")
data class RevisionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV_ID")
    var id: Long? = null,

    @RevisionTimestamp
    @Column(name = "REVTSTMP")
    var timestamp: LocalDateTime? = null
) : Serializable {

    private fun getRevisionDate(): LocalDateTime? = timestamp


    override fun toString(): String =
        "LongRevisionEntity(id = $id, revisionDate = ${getRevisionDate()?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}"

}