package ga.backend.team.service;


import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.team.entity.Team;
import ga.backend.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRespository;
    private final EmployeeRepository employeeRepository;

    // CREATE
    public Team createTeam(Team team) {
        return teamRespository.save(team);
    }

    // READ
    public Team findTeam(long teamPk) {
        Team team = verifiedTeam(teamPk);
        return team;
    }

    public List<Team> findTeams(Long teamPk, String teamName) {
        List<Team> teams;
        if (teamPk == null && teamName==null){
            teams = teamRespository.findAll();
        } else if (teamPk == null) {
            teams = teamRespository.findAllByTeamNameAndDelYnFalse(teamName);
        } else if(teamName == null) {
            teams = teamRespository.findAllByPkAndDelYnFalse(teamPk);
        } else {
            teams = teamRespository.findAllByPkAndTeamNameAndDelYnFalse(teamPk, teamName);
        }
        return teams;
    }

    // UPDATE
    public Team patchTeam(Team team) {
        Team findTeam = verifiedTeam(team.getPk());
        Optional.ofNullable(team.getTeamName()).ifPresent(findTeam::setTeamName);

        return teamRespository.save(findTeam);
    }

    // DELETE
    public void deleteTeam(long teamPk) {
        Team team = verifiedTeam(teamPk);
        teamRespository.delete(team);
    }

    // team에 소속된 직원 찾기
    public List<Employee> findEmployeeListByTeam(long teamPk) {
        Team team = verifiedTeam(teamPk);
        return employeeRepository.findEmployeesByTeamAndDelYnFalse(team);
    }

    // 검증
    public Team verifiedTeam(long teamPk) {
        Optional<Team> team = teamRespository.findByPkAndDelYnFalse(teamPk);
        return team.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TEAM_NOT_FOUND));
    }
}
