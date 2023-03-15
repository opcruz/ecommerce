package mx.ecommerce.services;

import mx.ecommerce.dtos.OrderResultDTO;
import mx.ecommerce.dtos.ProductOrderDTO;
import mx.ecommerce.dtos.ShoppingCartJoined;
import mx.ecommerce.models.Order;
import mx.ecommerce.models.OrderDetails;
import mx.ecommerce.repositories.OrderDetailsRepository;
import mx.ecommerce.repositories.OrderRepository;
import mx.ecommerce.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private OrderDetailsRepository orderDetailsRepository;

    private ShoppingCartRepository shoppingCartRepository;

    public OrderService(@Autowired OrderRepository orderRepository,
                        @Autowired OrderDetailsRepository orderDetailsRepository,
                        @Autowired ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Transactional(rollbackFor = {SQLException.class})
    public Order generateOrder(int clientId, String payment_method) {
        Iterable<ShoppingCartJoined> shoppingCartJList = shoppingCartRepository.filterByClientId(clientId);

        double total = 0.0f;
        LinkedList<Integer> deletedCartIds = new LinkedList<>();
        for (ShoppingCartJoined c : shoppingCartJList) {
            total += c.getQuantity() * c.getStock().getPrice();
            deletedCartIds.add(c.getId());
        }

        Order o = new Order();
        o.setCreated_at(new Timestamp(System.currentTimeMillis()));
        o.setStatus("pagado");
        o.setPayment_method(payment_method);
        o.setTotal(total);
        o.setClient_id(clientId);
        Order orderSaved = orderRepository.save(o);

        OrderDetails details;
        LinkedList<OrderDetails> list = new LinkedList<>();
        for (ShoppingCartJoined cart : shoppingCartJList) {
            details = new OrderDetails();
            details.setOrder_id(orderSaved.getId());
            details.setProduct_code(cart.getStock().getCode());
            details.setQuantity(cart.getQuantity());
            details.setPrice(cart.getStock().getPrice());
            list.add(details);
        }
        orderDetailsRepository.saveAll(list);
        shoppingCartRepository.deleteAllById(deletedCartIds);
        return orderSaved;
    }

    public Optional<OrderResultDTO> orderDetails(int clientId, int orderId) {
        Optional<Order> order = orderRepository.findByIdAndClientId(clientId, orderId);
        return order.map(value -> {
            Iterable<ProductOrderDTO> products = orderDetailsRepository.findByOrderId(value.getId());
            return new OrderResultDTO(value.getId(), value.getStatus(), value.getPayment_method(),
                    value.getTotal(), value.getCreated_at(), products);
        });
    }

}
