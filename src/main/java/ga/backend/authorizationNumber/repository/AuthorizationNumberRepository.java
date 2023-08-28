package ga.backend.authorizationNumber.repository;

import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationNumberRepository extends JpaRepository<AuthorizationNumber, String> {
}
