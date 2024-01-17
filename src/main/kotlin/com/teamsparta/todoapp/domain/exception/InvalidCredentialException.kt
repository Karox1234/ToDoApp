package com.teamsparta.todoapp.domain.exception

data class InvalidCredentialException(
    override val message: String =  "The credential is invalid"
): RuntimeException()
