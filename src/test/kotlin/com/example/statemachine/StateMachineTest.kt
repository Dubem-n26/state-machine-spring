package com.example.statemachine

import com.example.statemachine.config.Events
import com.example.statemachine.config.States
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.state.StateMachineState
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
    fun test_stateMachine_transitions_to_VALIDATION_SUCCESS() {
        System.out.println("initial state " + stateMachine.state)
        val subMachineState = (stateMachine.state as StateMachineState).submachine.state
            .id.toString()

        while (stateMachine.state.isSubmachineState && subMachineState !== States.VALIDATE_OVERDRAFT.toString()) {
            stateMachine.sendEvent(Events.VALIDATE_NEXT)
        }
        stateMachine.sendEvent(Events.VALIDATE_NEXT)
        assertEquals("", stateMachine.state.id, States.VALIDATION_SUCCESS)
    }

    @Test
    fun test_stateMachine_still_IN_VALIDATION_state() {
        System.out.println("initial state " + stateMachine.state.id.toString())
        stateMachine.sendEvent(Events.VALIDATE_NEXT)
        System.out.println("state " + stateMachine.state.id.toString())
        assertEquals("Validation state", stateMachine.state.id, States.IN_VALIDATION)
    }

}
