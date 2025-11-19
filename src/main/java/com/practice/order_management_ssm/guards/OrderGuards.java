package com.practice.order_management_ssm.guards;

import com.practice.order_management_ssm.enums.OrderEvents;
import com.practice.order_management_ssm.enums.OrderStates;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class OrderGuards {

    public Guard<OrderStates, OrderEvents> paymentIdGuard() {
        return context -> {
            // Pega um cabeçalho enviado junto com o evento
            String paymentId = (String) context.getMessageHeader("payment_id");
            // ID não for nulo e não estiver vazio: RETORNA TRUE
            // true: Permite a transição
            // falso: a transição é BLOQUEADA.
            return paymentId != null && !paymentId.isEmpty();
        };
    }
}