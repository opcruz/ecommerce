package mx.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginRequestDTO {
    String email;
    String password;

}
