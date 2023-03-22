package mx.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "stock") // This tells Hibernate to make a table out of this class
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    private String description;

    @JsonIgnore
    private byte[] image;

    private String color;

    private String category;

    private Integer quantity;

    private Double price;

    private String status;

    private Integer created_by;

    private Integer updated_by;

    public Stock() {
    }

    public Stock(Integer code, String description, String color, String category, Integer quantity,
                 Double price, String status, Integer created_by, Integer updated_by) {
        this.code = code;
        this.description = description;
        this.color = color;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Integer getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Integer updated_by) {
        this.updated_by = updated_by;
    }
}
