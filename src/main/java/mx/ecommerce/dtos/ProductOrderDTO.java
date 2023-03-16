package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import mx.ecommerce.models.Stock;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOrderDTO {

    private Integer quantity;
    private Double price;
    private Stock stock;

    public ProductOrderDTO() {
    }

    public ProductOrderDTO(Integer quantity, Double price,
                           Integer code, String description, String color, String category, String status) {
        this.quantity = quantity;
        this.price = price;

        Stock s = new Stock();
        s.setCode(code);
        s.setDescription(description);
        s.setColor(color);
        s.setCategory(category);
        s.setStatus(status);
        this.stock = s;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
