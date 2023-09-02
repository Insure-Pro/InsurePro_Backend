package ga.backend.li.repository;

import ga.backend.li.entity.Li;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiRepository extends JpaRepository<Li, Long> {
    List<Li> findByDong_Pk(long dongPk);
    Li findByLiAndDong_Pk(String liName, long dongPk);
}
