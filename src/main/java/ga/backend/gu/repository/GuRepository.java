package ga.backend.gu.repository;

import ga.backend.gu.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuRepository extends JpaRepository<Gu, Long> {
    List<Gu> findByMetro_Pk(long metroPk);
    Gu findByGuAndMetro_Pk(String guName, long metroPk);
}