package ga.backend.dong.repository;

import ga.backend.dong.entity.Dong;
import ga.backend.dong.mapper.DongMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DongRepository extends JpaRepository<Dong, Long> {
    List<Dong> findByGu_Pk(long guPk);
    Optional<Dong> findByDongAndGu_Pk(String dongName, long guPk);
    Optional<Dong> findByDong(String dongName);
}
