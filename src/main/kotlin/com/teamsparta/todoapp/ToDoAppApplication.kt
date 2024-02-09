package com.teamsparta.todoapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableJpaAuditing
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
class ToDoAppApplication

fun main(args: Array<String>) {
	runApplication<ToDoAppApplication>(*args)
}
