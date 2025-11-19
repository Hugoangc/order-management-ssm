package com.practice.order_management_ssm.actions;


import com.practice.order_management_ssm.enums.OrderEvents;
import com.practice.order_management_ssm.enums.OrderStates;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;


@Component
public class OrderActions {

    public Action<OrderStates, OrderEvents> shipOrderAction() {
        return context -> {
            System.out.println("Shipping Order");
        };
    }

    public Action<OrderStates, OrderEvents> payOrderAction() {
        return context -> {
            System.out.println("Paying Order");
        };
    }

    public Action<OrderStates, OrderEvents> validateOrderAction() {
        return context -> {
            System.out.println("Validating Order");
        };
    }
}
