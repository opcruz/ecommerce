package mx.ecommerce.models;

import jakarta.persistence.*;

@Entity(name = "shopping_cart") // This tells Hibernate to make a table out of this class
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "product_code")
//    private Integer product_code;

    private Integer quantity;

    private Integer client_id;

    @ManyToOne(targetEntity = Stock.class)
    @JoinColumn(name = "product_code", referencedColumnName = "code")
    private Stock stock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Integer getProduct_code() {
//        return product_code;
//    }
//
//    public void setProduct_code(Integer product_code) {
//        this.product_code = product_code;
//    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getClient_id() {
        return client_id;
    }

    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
