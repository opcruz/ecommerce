package mx.ecommerce.controllers;

import mx.ecommerce.repositories.StockRepository;
import mx.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/test") // This means URL's start with /demo (after Application path)
public class TestController {
    private final StockRepository stockRepository;
    private final OrderService orderService;

    public TestController(@Autowired StockRepository stockRepository,
                          @Autowired OrderService orderService) {
        this.stockRepository = stockRepository;
        this.orderService = orderService;
    }

    @GetMapping(path = "/transanction")
    public @ResponseBody String transanction() {
        // This returns a JSON or XML with the users
        try {
            orderService.transaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Hello";
    }

    @PostMapping(path = "/createOrder")
    public @ResponseBody String createOrder() {
        // This returns a JSON or XML with the users
        try {
            orderService.createOrder();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Hello";
    }

    @PostMapping(path = "/test")
    public @ResponseBody String test() {
        // This returns a JSON or XML with the users
        orderService.test(1);
        return "Hello";
    }

}
