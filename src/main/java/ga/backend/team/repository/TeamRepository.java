package ga.backend.team.repository;

import ga.backend.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByPk(Long pk);
    List<Team> findAllByTeamName(String teamName);
    List<Team> findAllByPkAndTeamName(Long pk, String teamName);
}
