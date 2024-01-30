package com.teamsparta.todoapp.infra.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.todoapp.domain.exception.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val objectMapper = ObjectMapper()
        val jsonString = objectMapper.writeValueAsString(ErrorResponse(accessDeniedException.message ?: "No permission to run API"))
        //아무런 메시지를 입력하지 않을경우 기본 메시지 "No permission to run API" 출력하고 내가 원하는 메시지를 적어서 출력하게 변경
        //기존엔 무조건 어떤 메시지를 적어도 No permission to run API 만 출력됫으나, 좀 더 상세한 안내를 위해 변경
        response.writer.write(jsonString)
    }
}