package com.example.statemachine

import com.example.statemachine.config.Events
import com.example.statemachine.config.States
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import java.util.*

@SpringBootTest
class StateMachineTest {

    @Autowired
    private lateinit var factory: StateMachineFactory<States, Events>

    @Test
    fun testStateMachine() {
        val stateMachine: StateMachine<States, Events> = factory.getStateMachine(UUID.randomUUID())
        stateMachine.start()

        System.out.println("initial state " + stateMachine.state.id.toString())
        while (stateMachine.state.id.toString() !== States.VALIDATION_SUCCESS.toString()) {
            stateMachine.sendEvent(Events.VALIDATE_NEXT)
            System.out.println("state " + stateMachine.state.id.toString())
        }

    }

}
