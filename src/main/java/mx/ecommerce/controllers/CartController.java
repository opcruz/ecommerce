package mx.ecommerce.controllers;

import io.swagger.v3.oas.annotations.Operation;
import mx.ecommerce.dtos.ProductCartDTO;
import mx.ecommerce.dtos.ShoppingCartJoined;
import mx.ecommerce.dtos.ShoppingCartResultDTO;
import mx.ecommerce.models.Order;
import mx.ecommerce.models.ShoppingCart;
import mx.ecommerce.repositories.ShoppingCartRepository;
import mx.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/carts")
public class CartController {
    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderService orderService;


    public CartController(@Autowired ShoppingCartRepository shoppingCartRepository,
                          @Autowired OrderService orderService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderService = orderService;
    }

    @GetMapping(path = "")
    @Operation(summary = "List cart products")
    public @ResponseBody ShoppingCartResultDTO getShoppingCart(@RequestParam Integer clientId) {
        Iterable<ShoppingCartJoined> shoppingCarts = shoppingCartRepository.filterByClientId(clientId);

        ShoppingCartResultDTO result = new ShoppingCartResultDTO();
        result.setClientId(clientId);

        double total = 0.0;
        LinkedList<ProductCartDTO> productsCart = new LinkedList<>();
        for (ShoppingCartJoined c : shoppingCarts) {
            total += c.getQuantity() * c.getStock().getPrice();
            productsCart.add(new ProductCartDTO(c.getId(), c.getQuantity(), c.getStock()));
        }
        result.setTotal(total);
        result.setProducts(productsCart);

        return result;
    }

    @DeleteMapping(path = "/{cartId}")
    @Operation(summary = "Delete cart product")
    public ResponseEntity<Void> deleteStock(@PathVariable int cartId, @RequestParam Integer clientId) {
        try {
            Integer integer = shoppingCartRepository.deleteByIdAndClientId(cartId, clientId);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add cart product")
    public @ResponseBody ShoppingCart addCartProduct(@RequestParam Integer clientId,
                                                     @RequestParam Integer productCode,
                                                     @RequestParam Integer quantity) {

        ShoppingCart shoppingCartProduct = new ShoppingCart();
        shoppingCartProduct.setProduct_code(productCode);
        shoppingCartProduct.setQuantity(quantity);
        shoppingCartProduct.setClient_id(clientId);

        ShoppingCart saved = shoppingCartRepository.save(shoppingCartProduct);
        return saved;
    }

    @PostMapping(path = "/buy")
    @Operation(summary = "Buy cart")
    public @ResponseBody Order buyCart(@RequestParam Integer clientId,
                                       @RequestParam String payment_method) {

        Order order = orderService.generateOrder(clientId, payment_method);
        return order;
    }

}
