package mx.ecommerce.services;

import mx.ecommerce.models.Order;
import mx.ecommerce.models.OrderDetails;
import mx.ecommerce.models.ShoppingCart;
import mx.ecommerce.models.Stock;
import mx.ecommerce.repositories.OrderDetailsRepository;
import mx.ecommerce.repositories.OrderRepository;
import mx.ecommerce.repositories.ShoppingCartRepository;
import mx.ecommerce.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {

    private StockRepository stockRepository;

    private OrderRepository orderRepository;

    private OrderDetailsRepository orderDetailsRepository;

    private ShoppingCartRepository shoppingCartRepository;


    @Transactional(rollbackFor = {SQLException.class})
    public Order test(int clientId) {
        shoppingCartRepository.findAll().forEach(x -> {
            System.out.println(x);
        });
        Iterable<ShoppingCart> shoppingCarts = shoppingCartRepository.filterByClientId(clientId);
//        shoppingCartRepository.deleteAll(shoppingCarts);

        float total = 0.0f;
        for (ShoppingCart c : shoppingCarts) {
            total += c.getQuantity() * c.getStock().getPrice();
        }

        Order o = new Order();
        o.setCreated_at(new Timestamp(System.currentTimeMillis()));
        o.setStatus("pagado");
        o.setPayment_method("paypal");
        o.setTotal(total);
        o.setClient_id(clientId);
        Order orderSaved = orderRepository.save(o);

        OrderDetails details;
        LinkedList<OrderDetails> list = new LinkedList<>();
        for (ShoppingCart cart : shoppingCarts) {
            details = new OrderDetails();
            details.setOrder_id(orderSaved.getId());
            details.setProduct_code(cart.getStock().getCode());
            details.setQuantity(cart.getQuantity());
            details.setPrice(cart.getStock().getPrice());
            list.add(details);
        }
        orderDetailsRepository.saveAll(list);
        return orderSaved;
    }


    public OrderService(@Autowired StockRepository stockRepository,
                        @Autowired OrderRepository orderRepository,
                        @Autowired OrderDetailsRepository orderDetailsRepository,
                        @Autowired ShoppingCartRepository shoppingCartRepository) {
        this.stockRepository = stockRepository;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void createOrder() throws SQLException {
        Order o = new Order();
        o.setCreated_at(new Timestamp(System.currentTimeMillis()));
        o.setStatus("pagado");
        o.setPayment_method("paypal");
        o.setTotal(50.0f);
        o.setClient_id(1);
        Order orderSaved = orderRepository.save(o);

        OrderDetails details = new OrderDetails();
        details.setOrder_id(orderSaved.getId());
        details.setProduct_code(1);
        details.setQuantity(100);
        details.setPrice(25.00);

        OrderDetails details2 = new OrderDetails();
        details2.setOrder_id(orderSaved.getId());
        details2.setProduct_code(1);
        details2.setQuantity(50);
        details2.setPrice(16.00);
//        throw new SQLException("Throwing exception for demoing Rollback!!!");
        List<OrderDetails> orderDetails = Arrays.asList(details, details2);
        System.out.println(orderDetails.size());
        orderDetailsRepository.saveAll(orderDetails);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void transaction() throws SQLException {
        stockRepository.deleteById(48);
        Stock k = new Stock();
        k.setCategory("comida");
        k.setColor("color");
        k.setPrice(25.45);
        k.setDescription("description");
        k.setQuantity(89);
        k.setStatus("status");
//        throw new SQLException("Throwing exception for demoing Rollback!!!");
        stockRepository.save(k);
    }

}
