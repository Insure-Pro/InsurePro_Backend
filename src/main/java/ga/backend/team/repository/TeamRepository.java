package ga.backend.team.repository;

import ga.backend.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByPkAndDelYnFalse(Long pk);
    List<Team> findAllByPkAndDelYnFalse(Long pk);
    List<Team> findAllByTeamNameAndDelYnFalse(String teamName);
    List<Team> findAllByPkAndTeamNameAndDelYnFalse(Long pk, String teamName);
}
