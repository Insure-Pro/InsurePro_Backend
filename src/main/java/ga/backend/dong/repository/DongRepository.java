package ga.backend.dong.repository;

import ga.backend.dong.entity.Dong;
import ga.backend.dong.mapper.DongMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DongRepository extends JpaRepository<Dong, Long> {
}
