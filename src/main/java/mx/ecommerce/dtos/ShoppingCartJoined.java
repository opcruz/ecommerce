package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import mx.ecommerce.models.Stock;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartJoined {

    private Integer id;

    private Stock stock;

    private Integer quantity;

    public ShoppingCartJoined(Integer id, Integer quantity,
                              Integer code, String description, String color, String category, Double price, String status) {

        this.id = id;
        this.quantity = quantity;

        Stock s = new Stock();
        s.setCode(code);
        s.setDescription(description);
        s.setColor(color);
        s.setCategory(category);
        s.setPrice(price);
        s.setStatus(status);
        this.stock = s;
    }


    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
