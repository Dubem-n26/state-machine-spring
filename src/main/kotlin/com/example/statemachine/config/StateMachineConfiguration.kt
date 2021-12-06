package com.example.statemachine.config

import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer

@Configuration
@EnableStateMachineFactory
class StateMachineConfiguration : EnumStateMachineConfigurerAdapter<States, Events>() {

    override fun configure(states: StateMachineStateConfigurer<States, Events>) {
        states
            .withStates()
            .initial(States.VALIDATE_SPACES)
            .state(States.VALIDATE_SPACES)
            .state(States.VALIDATE_PACCOUNTS)
            .state(States.VALIDATE_OVERDRAFT)
            .state(States.VALIDATION_SUCCESS)
            .state(States.VALIDATION_FAILED)
            .and()
            .withStates()
            .parent(States.IN_VALIDATION)
            .initial(States.VALIDATE_SPACES)
            .state(States.VALIDATE_PACCOUNTS)
            .state(States.VALIDATE_OVERDRAFT)
    }

    override fun configure(transitions: StateMachineTransitionConfigurer<States, Events>) {
        transitions
            .withExternal()
            .source(States.VALIDATE_SPACES).target(States.VALIDATE_PACCOUNTS).event(Events.VALIDATE_NEXT)
            .and()
            .withExternal()
            .source(States.VALIDATE_PACCOUNTS).target(States.VALIDATE_OVERDRAFT).event(Events.VALIDATE_NEXT)
            .and()
            .withExternal()
            .source(States.VALIDATE_OVERDRAFT).target(States.VALIDATION_SUCCESS).event(Events.VALIDATE_NEXT)
            .and()
            .withExternal()
            .source(States.VALIDATE_SPACES).target(States.VALIDATION_FAILED).event(Events.VALIDATE_ERROR)
            .and()
            .withExternal()
            .source(States.VALIDATE_PACCOUNTS).target(States.VALIDATION_FAILED).event(Events.VALIDATE_ERROR)
            .and()
            .withExternal()
            .source(States.VALIDATE_OVERDRAFT).target(States.VALIDATION_FAILED).event(Events.VALIDATE_ERROR)
    }

}

enum class States {
    IN_VALIDATION, VALIDATE_SPACES, VALIDATE_PACCOUNTS, VALIDATE_OVERDRAFT,
    VALIDATION_FAILED, VALIDATION_SUCCESS
}

enum class Events {
    VALIDATE_NEXT, VALIDATE_ERROR
}
