package ga.backend.gu.repository;

import ga.backend.gu.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuRepository extends JpaRepository<Gu, Long> {
}
