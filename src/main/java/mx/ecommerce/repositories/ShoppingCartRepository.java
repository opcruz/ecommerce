package mx.ecommerce.repositories;

import mx.ecommerce.models.ShoppingCart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer> {

    @Query(value = "SELECT u FROM shopping_cart u JOIN u.stock s WHERE u.client_id = :client_id")
    Iterable<ShoppingCart> filterByClientId(@Param("client_id") Integer client_id);


}
