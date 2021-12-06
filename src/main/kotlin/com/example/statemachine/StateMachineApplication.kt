package com.example.statemachine

import com.example.statemachine.StateMachine
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.statemachine.config.EnableStateMachine

@SpringBootApplication
@EnableStateMachine
class StateMachineApplication

fun main(args: Array<String>) {
	runApplication<StateMachineApplication>(*args)
}
