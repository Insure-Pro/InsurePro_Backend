package ga.backend.dong.repository;

import ga.backend.dong.entity.Dong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByGu_Pk(long guPk);
    Optional<Dong> findByDongNameAndGu_Pk(String dongName, long guPk);
    Optional<Dong> findByDongName(String dongName);
}
