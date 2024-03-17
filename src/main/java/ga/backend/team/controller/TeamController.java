package ga.backend.team.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.company.dto.CompanyResponseDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.team.dto.TeamRequestDto;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.entity.Team;
import ga.backend.team.mapper.TeamMapper;
import ga.backend.team.service.TeamService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final EmployeeMapper employeeMapper;

    // CREATE
    @PostMapping("/team")
    public ResponseEntity postTeam(@Valid @RequestBody TeamRequestDto.Post post) {
        Team team = teamService.createTeam(teamMapper.teamPostDtoToTeam(post), post.getCompanyPk());
        TeamResponseDto.Response response = teamMapper.teamToTeamResponseDto(team, team.getCompany().getPk());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ : 모든 팀 조회
    @GetMapping("/teams")
    public ResponseEntity getTeamList (@Positive @RequestParam(value = "pk", required = false) Long teamPk,
                                       @RequestParam(value = "name", required = false) String teamName) {
        List<Team> teams = teamService.findTeams(teamPk, teamName);

        // 응답
        List<TeamResponseDto.Response> teamResponse = teamMapper.teamToTeamListResponseDto(teams);
        JSONObject response = new JSONObject();
        response.put("teams", teamResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // READ : 팀별 직원 조회
    @GetMapping("/team/employees/{team-pk}")
    public ResponseEntity getEmployeeListByTeam (@Positive @PathVariable(value = "team-pk") long teamPk) {

        List<Employee> employees = teamService.findEmployeeListByTeam(teamPk);

        // 응답
        List<EmployeeResponseDto.Response> employeeResponse = employeeMapper.employeeToEmployeeListResponseDto(employees);
        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/team/{team-pk}")
    public ResponseEntity patchTeam(@Positive @PathVariable("team-pk") long teamPk,
                                        @Valid @RequestBody TeamRequestDto.Patch patch) {
        patch.setPk(teamPk);
        Team team = teamService.patchTeam(teamMapper.teamPatchDtoToTeam(patch), patch.getCompanyPk());
        TeamResponseDto.Response response = teamMapper.teamToTeamResponseDto(team, team.getCompany().getPk());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/team/{team-pk}")
    public ResponseEntity deleteTeam(@Positive @PathVariable("team-pk") long teamPk) {
        teamService.deleteTeam(teamPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
