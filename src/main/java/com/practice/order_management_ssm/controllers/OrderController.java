package com.practice.order_management_ssm.controllers;

import com.practice.order_management_ssm.enums.OrderStates;
import com.practice.order_management_ssm.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    //BRINCANDO COM GUARD
    //postman: http://localhost:8080/order/guard/null
    @PostMapping({ "/guard", "/guard/{id}" })
    public String testGuard(@PathVariable("id") String paymentId) {

        String id = "null".equals(paymentId) ? null : paymentId;
        OrderStates estadoFinal = orderService.testPaymentGuard(1L, id);
        if (id != null) {
            return "Tentei pagar COM ID: " + id + ". Estado Final: " + estadoFinal;
        } else {
            return "Tentei pagar SEM ID. Estado Final: " + estadoFinal;
        }
    }

    @PostMapping("new")
    public String newOrder(){
        orderService.newOrder();
        return "newOrder";
    }

    @PostMapping("pay")
    public String payOrder(){
        orderService.payOrder();
        return "payOrder";
    }

    @PostMapping("ship")
    public String shipOrder(){
        orderService.shipOrder();
        return "shipOrder";
    }

    @PostMapping("complete")
    public String completeOrder(){
        orderService.completeOrder();
        return "completeOrder";
    }
}
