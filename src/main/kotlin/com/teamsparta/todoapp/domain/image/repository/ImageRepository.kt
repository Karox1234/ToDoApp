package com.teamsparta.todoapp.domain.image.repository

import com.teamsparta.todoapp.domain.image.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {

    fun findByUrl(url:String) : Image
}