package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import mx.ecommerce.models.Stock;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ShoppingCartJoined {

    private Integer id;

    private Stock stock;

    private Integer quantity;

    public ShoppingCartJoined(Integer id, Integer quantity,
                              Integer code, String description, String color, String category, Double price, String status) {

        this.id = id;
        this.quantity = quantity;
        this.stock =
                Stock.builder()
                        .code(code)
                        .description(description)
                        .color(color)
                        .category(category)
                        .price(price)
                        .status(status)
                        .build();
    }

}
