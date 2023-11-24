package ga.backend.dong2.repository;

import ga.backend.dong2.entity.Dong2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Dong2Repository extends JpaRepository<Dong2, Long> {
    List<Dong2> findByGu2_Pk(long gu2Pk);
    Optional<Dong2> findByDongNameAndGu2_Pk(String dongName, long gu2Pk);
    Optional<Dong2> findByDongName(String dongName);
}
