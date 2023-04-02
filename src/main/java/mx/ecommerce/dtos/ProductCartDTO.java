package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import mx.ecommerce.models.Stock;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ProductCartDTO {

    private Integer cartId;

    private Integer quantity;
    private Stock stock;

}
