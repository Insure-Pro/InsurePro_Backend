package ga.backend.gu.repository;

import ga.backend.gu.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuRepository extends JpaRepository<Gu, Long> {
    List<Gu> findByMetro_Pk(long metroPk);
    Optional<Gu> findByGuAndMetro_Pk(String guName, long metroPk);
    Optional<Gu> findByGu(String guName);
}