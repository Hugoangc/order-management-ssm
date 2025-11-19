package com.practice.order_management_ssm.services;


import com.practice.order_management_ssm.enums.OrderEvents;
import com.practice.order_management_ssm.enums.OrderStates;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final StateMachineFactory<OrderStates, OrderEvents> stateMachineFactory;
    private StateMachine<OrderStates, OrderEvents> stateMachine;

    public OrderStates testPaymentGuard(Long orderId, String paymentIdInput) {
        StateMachine<OrderStates, OrderEvents> currentStateMachine = stateMachineFactory.getStateMachine(orderId.toString());
        currentStateMachine.startReactively().subscribe();

        System.out.println("--- INICIANDO TESTE ---");

        //  Validar
        currentStateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.VALIDATE).build())).subscribe();
        System.out.println("Estado Atual: " + currentStateMachine.getState().getId());

        System.out.println("Tentando pagar com ID: " + (paymentIdInput == null ? "SEM ID" : paymentIdInput));

        //  Tentar Pagar usando o ID que veio do Postman
        MessageBuilder<OrderEvents> messageBuilder = MessageBuilder.withPayload(OrderEvents.PAY);
        System.out.println("Estado pós-tentativa: " + currentStateMachine.getState().getId());
        System.out.println("Para falha deve ser: VALIDATED");

        // Só adiciona o header se o usuário mandou algo diferente de nulo
        if (paymentIdInput != null) {
            messageBuilder.setHeader("payment_id", paymentIdInput);
        }

        currentStateMachine.sendEvent(Mono.just(messageBuilder.build())).subscribe();

        System.out.println("Estado Final: " + currentStateMachine.getState().getId());
        return currentStateMachine.getState().getId();
    }

    public void newOrder() {
        StateMachine<OrderStates, OrderEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        sm.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.VALIDATE).build())).subscribe();
    }

//    public void newOrder(){
//        initOrderSaga();
//        validateOrder();
//    }

    public void validateOrder() {
        System.out.print("Validating order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEvents.VALIDATE).build()
        )).subscribe(result ->
            System.out.println(result.getResultType()));

        System.out.println("Final state: " + stateMachine.getState().getId());
    }
    public void payOrder() {
        System.out.print("Validating order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEvents.PAY).build()
        )).subscribe(result ->
                System.out.println(result.getResultType()));

        System.out.println("Final state: " + stateMachine.getState().getId());
    }
    public void shipOrder() {
        System.out.print("Validating order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEvents.SHIP).build()
        )).subscribe(result ->
                System.out.println(result.getResultType()));

        System.out.println("Final state: " + stateMachine.getState().getId());
    }

    public void completeOrder() {
        System.out.print("Validating order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEvents.COMPLETE).build()
        )).subscribe(result ->
                System.out.println(result.getResultType()));

        System.out.println("Final state: " + stateMachine.getState().getId());
        stopOrderSaga();
    }


    public void initOrderSaga() {
        System.out.print("Initializing Order Saga...");
        stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.startReactively().subscribe();
        System.out.println("Final state: " + stateMachine.getState().getId());
    }

    public void stopOrderSaga() {
        System.out.print("Stopping Order Saga...");
        stateMachine.stopReactively().subscribe();
    }


}
