package com.teamsparta.todoapp.domain.image.service

import com.teamsparta.todoapp.domain.image.model.Image
import com.teamsparta.todoapp.domain.image.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@Service
class ImageService(private val imageRepository: ImageRepository) {

    fun uploadImage(imageFile: MultipartFile): String {
        //랜덤한 파일명 생성 (파일명 중복 방지,랜덤코드_원래 파일명)
        val fileName = "${UUID.randomUUID()}_${imageFile.originalFilename}"

        //이미지 파일을 저장할 경로 설정
        val uploadDir = Paths.get("src/main/resources/uploads")

        //파일 저장 경로 설정
        val filePath: Path = uploadDir.resolve(fileName)

        //없으면 경로 생성해줌
        Files.createDirectories(filePath.parent)

        //파일을 복사해서 경로에 저장
        Files.copy(imageFile.inputStream, filePath)

        //파일의 URL 생성
        val url = "/src/main/resources/uploads/$fileName"

        // 이미지 엔터티 생성 및 저장 (이미지 파일의 URL만 데이터베이스에 저장)
//        val image = Image(fileName = fileName, card = null,url = url)
//        imageRepository.save(image) 단독으로 사용시 쓰는코드, create쪽에서 사용할땐 save 중복 문제로 비활성화

        Image(fileName = fileName, card = null, url = url)

        return url
    }

}

