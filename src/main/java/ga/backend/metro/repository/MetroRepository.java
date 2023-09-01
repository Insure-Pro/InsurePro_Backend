package ga.backend.metro.repository;

import ga.backend.metro.entity.Metro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetroRepository extends JpaRepository<Metro, Long> {
}
