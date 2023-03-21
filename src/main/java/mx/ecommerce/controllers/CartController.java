package mx.ecommerce.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mx.ecommerce.dtos.ProductCartDTO;
import mx.ecommerce.dtos.ShoppingCartJoined;
import mx.ecommerce.dtos.ShoppingCartResultDTO;
import mx.ecommerce.models.Order;
import mx.ecommerce.models.ShoppingCart;
import mx.ecommerce.repositories.ShoppingCartRepository;
import mx.ecommerce.repositories.StockRepository;
import mx.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Optional;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/carts")
public class CartController {
    private final ShoppingCartRepository shoppingCartRepository;
    private final StockRepository stockRepository;

    private final OrderService orderService;


    public CartController(@Autowired ShoppingCartRepository shoppingCartRepository,
                          @Autowired OrderService orderService,
                          @Autowired StockRepository stockRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderService = orderService;
        this.stockRepository = stockRepository;
    }

    @GetMapping(path = "")
    @Operation(summary = "List cart products", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ShoppingCartResultDTO getShoppingCart(Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);
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
    @Operation(summary = "Delete cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStock(@RequestParam Integer cartId,
                                            Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);
        try {
            Integer integer = shoppingCartRepository.deleteByIdAndClientId(cartId, clientId);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ShoppingCart> addCartProduct(@RequestParam Integer productCode,
                                                       @RequestParam Integer quantity,
                                                       Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);

        ShoppingCart shoppingCartProduct = new ShoppingCart();
        shoppingCartProduct.setProduct_code(productCode);
        shoppingCartProduct.setQuantity(quantity);
        shoppingCartProduct.setClient_id(clientId);

        Optional<ShoppingCart> objectOptional = stockRepository.findById(productCode).flatMap(product -> {
            if (product.getQuantity() >= quantity) {
                ShoppingCart saved = shoppingCartRepository.save(shoppingCartProduct);
                return Optional.of(saved);
            } else {
                return Optional.empty();
            }
        });

        return objectOptional.map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping(path = "/buy")
    @Operation(summary = "Buy cart", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Order buyCart(@RequestParam String payment_method,
                                       Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);
        Order order = orderService.generateOrder(clientId, payment_method);
        return order;
    }

}
