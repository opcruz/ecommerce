package mx.ecommerce.repositories;

import mx.ecommerce.models.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

    @Query(value = "SELECT * FROM stock u WHERE u.color = :color LIMIT 1", nativeQuery = true)
    Optional<Stock> findStockByColor(@Param("color") String color);

    @Query(value = "SELECT new mx.ecommerce.models.Stock(u.code, u.description, u.color, u.category, u.quantity, u.price, u.status) FROM stock u")
    Iterable<Stock> allWithoutImage();

    @Query(value = "SELECT new mx.ecommerce.models.Stock(u.code, u.description, u.color, u.category, u.quantity, u.price, u.status)" +
            " FROM stock u WHERE u.description LIKE %:phrase%")
    Iterable<Stock> findByName(@Param("phrase") String phrase);

    @Query(value = "SELECT u.image FROM stock u WHERE u.code = :code")
    Optional<byte[]> findImageByCode(@Param("code") Integer code);

}
