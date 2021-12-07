package com.example.statemachine

import com.example.statemachine.config.Events
import com.example.statemachine.config.States
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.test.util.AssertionErrors.assertEquals
import java.util.*

@SpringBootTest
class StateMachineTest {

    @Autowired
    private lateinit var factory: StateMachineFactory<States, Events>

    private lateinit var stateMachine: StateMachine<States, Events>

    @BeforeEach
    fun beforeEach() {
        stateMachine = factory.getStateMachine(UUID.randomUUID())
        stateMachine.start()
    }

    @Test
    fun test_stateMachine_happyPath() {
        System.out.println("initial state " + stateMachine.state.id.toString())
        while (stateMachine.state.id.toString() !== States.VALIDATION_SUCCESS.toString()) {
            stateMachine.sendEvent(Events.VALIDATE_NEXT)
            System.out.println("state " + stateMachine.state.id.toString())
        }
        assertEquals("Validation state", stateMachine.state.id.toString(), States.VALIDATION_SUCCESS.toString())
    }

    @Test
    fun test_stateMachine_failure_scenario() {
        System.out.println("initial state " + stateMachine.state.id.toString())

        stateMachine.sendEvent(Events.VALIDATE_ERROR)
        System.out.println("state " + stateMachine.state.id.toString())
        assertEquals("Validation state", stateMachine.state.id.toString(), States.VALIDATION_FAILED.toString())
    }

}
