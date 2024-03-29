package mx.ecommerce.repositories;

import mx.ecommerce.models.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    @Query(value = "SELECT u FROM clients u WHERE u.email = :email AND u.passwordhash = :password_hash")
    Optional<Client> loginClient(@Param("email") String email, @Param("password_hash") String passwordHash);

}
