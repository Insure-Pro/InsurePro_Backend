package ga.backend.li.repository;

import ga.backend.li.entity.Li;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiRepository extends JpaRepository<Li, Long> {
    List<Li> findByDong_Pk(long dongPk);
    Optional<Li> findByLiAndDong_Pk(String liName, long dongPk);
    Optional<Li> findByLi(String liName);
}
