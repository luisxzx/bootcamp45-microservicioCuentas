package com.nttdata.bootcamp.s01accountservice.config.circuitbrakerLog;

import io.github.resilience4j.circuitbreaker.CircuitBreaker.StateTransition;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CircuitBreakerLogger {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerLogger.class);

    @EventListener
    public void onCircuitBreakerStateChange(CircuitBreakerOnStateTransitionEvent event) {
        String circuitBreakerName = event.getCircuitBreakerName();
        StateTransition stateTransition = event.getStateTransition();

        LOG.info("CircuitBreaker name: {}, newState: {}, oldState: {}",
                circuitBreakerName,
                stateTransition.getToState(),
                stateTransition.getFromState());
    }
}
