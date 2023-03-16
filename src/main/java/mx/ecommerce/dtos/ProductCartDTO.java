package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import mx.ecommerce.models.Stock;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCartDTO {

    private Integer cartId;

    private Integer quantity;
    private Stock stock;

    public ProductCartDTO() {
    }

    public ProductCartDTO(Integer cartId, Integer quantity, Stock stock) {
        this.cartId = cartId;
        this.quantity = quantity;
        this.stock = stock;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
