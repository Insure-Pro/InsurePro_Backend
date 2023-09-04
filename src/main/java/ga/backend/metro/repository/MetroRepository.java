package ga.backend.metro.repository;

import ga.backend.metro.entity.Metro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetroRepository extends JpaRepository<Metro, Long> {
    Optional<Metro> findByMetroName(String metroName);
}
