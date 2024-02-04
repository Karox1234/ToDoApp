package com.teamsparta.todoapp.domain.user.dto

data class SendMail(
    val email:String

)
{
    //이메일 형식 검사
    init {
        if (!isValidEmail(email)) {
            throw IllegalArgumentException("잘못된 이메일 주소 입니다 $email")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return email.matches(emailRegex.toRegex())
    }
}