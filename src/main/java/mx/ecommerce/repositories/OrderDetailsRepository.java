package mx.ecommerce.repositories;

import mx.ecommerce.dtos.ProductOrderDTO;
import mx.ecommerce.models.OrderDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {

    @Query(value = "SELECT new mx.ecommerce.dtos.ProductOrderDTO(u.quantity, u.price, s.code, s.description, s.color, s.category, s.status)" +
            " FROM order_details u JOIN stock s ON u.product_code = s.code WHERE u.order_id = :order_id")
    Iterable<ProductOrderDTO> findByOrderId(@Param("order_id") Integer clientId);

}
