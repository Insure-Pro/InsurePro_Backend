package ga.backend.gu2.repository;

import ga.backend.gu2.entity.Gu2;
import ga.backend.metro2.entity.Metro2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Gu2Repository extends JpaRepository<Gu2, Long> {
    List<Gu2> findByMetro2_Pk(long metro2Pk);
    Optional<Gu2> findByGuNameAndMetro2_Pk(String guName, long metro2Pk);
    Optional<Gu2> findByGuName(String guName);
    Optional<Gu2> findByGuNameAndMetro2(String guName, Metro2 metro2);
}