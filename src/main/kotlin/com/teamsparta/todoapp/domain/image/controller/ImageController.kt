package com.teamsparta.todoapp.domain.image.controller

import com.teamsparta.todoapp.domain.image.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

//@RestController
//@RequestMapping("/images")
//class ImageController(private val imageService: ImageService) {
//
//    @PostMapping("/upload",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],)
//    fun uploadImage(@RequestParam("file") imageFile: MultipartFile): ResponseEntity<String> {
//        val fileName = imageService.uploadImage(imageFile)
//        return ResponseEntity.status(HttpStatus.CREATED).body(fileName)
//    }
//
//}

//사용하지 않음, 실험용