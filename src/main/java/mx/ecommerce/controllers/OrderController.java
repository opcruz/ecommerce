package mx.ecommerce.controllers;

import io.swagger.v3.oas.annotations.Operation;
import mx.ecommerce.dtos.OrderResultDTO;
import mx.ecommerce.models.Order;
import mx.ecommerce.repositories.OrderRepository;
import mx.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderRepository orderRepository;

    private final OrderService orderService;


    public OrderController(@Autowired OrderRepository orderRepository,
                           @Autowired OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List orders")
    public @ResponseBody Iterable<Order> getShoppingCart(@RequestParam Integer clientId) {
        Iterable<Order> orders = orderRepository.findByClientId(clientId);
        return orders;
    }

    @GetMapping(path = "/{order_id}/details")
    @Operation(summary = "List orders")
    public @ResponseBody ResponseEntity<OrderResultDTO> orderDetails(@RequestParam Integer clientId,
                                                                     @PathVariable(value = "order_id") Integer orderId) {
        return orderService.orderDetails(clientId, orderId)
                .map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
    }


}
