package com.teamsparta.todoapp.infra.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class TestAop {

    @Around("execution(* com.teamsparta.todoapp.domain.cards.service.CardService.getAllCardList(..))")
    fun thisIsAdvice(joinPoint: ProceedingJoinPoint) : Any {
        println("AOP START!!!")
        val result:Any = joinPoint.proceed()
        println("AOP END!!!")
        return result
    }

}