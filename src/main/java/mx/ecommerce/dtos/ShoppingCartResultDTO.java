package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartResultDTO {

    private Integer clientId;
    private Double total;

    private Iterable<ProductCartDTO> products;

    public ShoppingCartResultDTO() {
    }

    public ShoppingCartResultDTO(Integer clientId, Double total, Iterable<ProductCartDTO> products) {
        this.clientId = clientId;
        this.total = total;
        this.products = products;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Iterable<ProductCartDTO> getProducts() {
        return products;
    }

    public void setProducts(Iterable<ProductCartDTO> products) {
        this.products = products;
    }
}
