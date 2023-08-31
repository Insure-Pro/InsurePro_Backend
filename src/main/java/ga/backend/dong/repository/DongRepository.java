package ga.backend.dong.repository;

import ga.backend.dong.entity.Dong;
import ga.backend.dong.mapper.DongMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByGu_Pk(long guPk);
    Dong findByDongAndGu_Pk(String dongName, long guPk);
}
