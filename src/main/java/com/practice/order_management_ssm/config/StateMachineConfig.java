package com.practice.order_management_ssm.config;


import com.practice.order_management_ssm.enums.OrderEvents;
import com.practice.order_management_ssm.enums.OrderStates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import com.practice.order_management_ssm.actions.OrderActions;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import com.practice.order_management_ssm.guards.OrderGuards;
import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {
    private final OrderActions orderActions;
    private final OrderGuards orderGuards;
    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception{
        states
                .withStates()
                .initial(OrderStates.NEW)
                .states(EnumSet.allOf(OrderStates.class))
                .end(OrderStates.COMPLETED)
                .end(OrderStates.CANCELLED);

    }
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception{
        transitions
                .withExternal().source(OrderStates.NEW).target(OrderStates.VALIDATED).event(OrderEvents.VALIDATE)
                .action(orderActions.validateOrderAction())
                .and()
                .withExternal().source(OrderStates.VALIDATED).target(OrderStates.PAID).event(OrderEvents.PAY)
                .action(orderActions.payOrderAction())
                .guard(orderGuards.paymentIdGuard())
                .and()
                .withExternal().source(OrderStates.PAID).target(OrderStates.SHIPPED).event(OrderEvents.SHIP)
                .action(orderActions.shipOrderAction())
                .and()
                .withExternal().source(OrderStates.SHIPPED).target(OrderStates.COMPLETED).event(OrderEvents.COMPLETE)
                .and()
                .withExternal().source(OrderStates.VALIDATED).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL)
                .and()
                .withExternal().source(OrderStates.PAID).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL);

    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception{
        config.withConfiguration().listener(stateMachineListener());
    }

    @Bean
    StateMachineListener<OrderStates, OrderEvents> stateMachineListener() {
        return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
            @Override
            public void transition(Transition<OrderStates, OrderEvents> transition){

                String source = transition.getSource() != null ? transition.getSource().getId().toString() : "EMPTY";
                String target = transition.getTarget() != null ? transition.getTarget().getId().toString() : "EMPTY";

                System.out
                        .println("Transitioning from " + source + " to " + target);

            }
        };
    }

}
