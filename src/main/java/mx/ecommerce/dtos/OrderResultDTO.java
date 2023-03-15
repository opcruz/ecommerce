package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResultDTO {

    private Integer id;
    private String status;
    private String paymentMethod;
    private Double total;
    private Timestamp createdAt;

    private Iterable<ProductOrderDTO> products;

    public OrderResultDTO() {
    }

    public OrderResultDTO(Integer id, String status, String paymentMethod, Double total, Timestamp createdAt, Iterable<ProductOrderDTO> products) {
        this.id = id;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.createdAt = createdAt;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Iterable<ProductOrderDTO> getProducts() {
        return products;
    }

    public void setProducts(Iterable<ProductOrderDTO> products) {
        this.products = products;
    }
}
