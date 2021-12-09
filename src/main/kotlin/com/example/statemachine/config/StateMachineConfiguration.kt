package com.example.statemachine.config

import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer

@Configuration
@EnableStateMachineFactory
class StateMachineConfiguration : EnumStateMachineConfigurerAdapter<States, Events>() {

    companion object {
        const val SPACES_VALIDATED: String = "spaces"
    }

    override fun configure(states: StateMachineStateConfigurer<States, Events>) {
        states
            .withStates()
            .initial(States.IN_VALIDATION)
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
            .source(States.IN_VALIDATION).target(States.VALIDATE_SPACES).event(Events.VALIDATE_NEXT)
            .and()
            .withLocal()
            .source(States.VALIDATE_SPACES).target(States.VALIDATE_PACCOUNTS).event(Events.VALIDATE_NEXT)
            .action(validateSpaces())
            .and()
            .withLocal()
            .source(States.VALIDATE_PACCOUNTS).target(States.VALIDATE_OVERDRAFT).event(Events.VALIDATE_NEXT)
            .action(finishValidation())
            .and()
            .withExternal()
            .source(States.IN_VALIDATION).target(States.VALIDATION_SUCCESS).event(Events.VALIDATE_SUCCESS)
            .and()
            .withExternal()
            .source(States.IN_VALIDATION).target(States.VALIDATION_FAILED).event(Events.VALIDATE_ERROR)
    }

}

fun finishValidation(): Action<States, Events> {
    return Action {
        if (it.extendedState.variables[StateMachineConfiguration.SPACES_VALIDATED] == true) {
            it.stateMachine.sendEvent(Events.VALIDATE_SUCCESS)
            return@Action
        }
        it.stateMachine.sendEvent(Events.VALIDATE_ERROR)
    }
}

fun validateSpaces(): Action<States, Events> {
    //the "spaces" variable here is used as the decision whether to emit VALIDATE_ERROR or VALIDATE_SUCCESS events
    //which would transition the state to either VALIDATION_FAILED or VALIDATION_SUCCESS
    return Action { it.extendedState.variables.put(StateMachineConfiguration.SPACES_VALIDATED, true) }
}

enum class States {
    IN_VALIDATION, VALIDATE_SPACES, VALIDATE_PACCOUNTS, VALIDATE_OVERDRAFT,
    VALIDATION_FAILED, VALIDATION_SUCCESS
}

enum class Events {
    VALIDATE_NEXT, VALIDATE_ERROR, VALIDATE_SUCCESS
}
