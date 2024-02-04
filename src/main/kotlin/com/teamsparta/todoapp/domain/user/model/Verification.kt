package com.teamsparta.todoapp.domain.user.model

import jakarta.persistence.*

@Table
@Entity
class Verification(

    @Column(nullable = false , name ="email")
    val email:String,

    @Column(nullable = false , name ="verification_code")
    val verificationCode : Int,

    @Column(nullable = false , name ="expiration_time")
    val expirationTime: Long
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}