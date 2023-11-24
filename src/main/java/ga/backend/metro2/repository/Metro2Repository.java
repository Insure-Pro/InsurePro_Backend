package ga.backend.metro2.repository;

import ga.backend.metro2.entity.Metro2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Metro2Repository extends JpaRepository<Metro2, Long> {
    Optional<Metro2> findByMetroName(String metroName);
}
